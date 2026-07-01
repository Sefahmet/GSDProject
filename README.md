# 🚴 Bikeability App — Considering Greenery and Effort by User Preferences

> A cyclist-centric navigation application developed as a Master's Project at the **University of Bonn** (Institute of Geodesy and Geoinformation), 2024.

**Authors:** Ahmet Sefa Altundal · Benoit Atayi · Julius Knechtel
**Supervisor:** Prof. Dr.-Ing. Jan Henrik Haunert
**Institution:** Institute of Geodesy and Geoinformation, University of Bonn
**Study Area:** Bonn, Germany
**Year:** 2024

> **Note:** This repository is maintained by **Ahmet Sefa Altundal**. The project was developed collaboratively as part of the Master's programme at the University of Bonn.

---

## 📖 Overview

Most navigation apps are designed around car drivers and optimize for the shortest or fastest route. This project takes a different approach: it builds a **multi-criteria routing engine specifically for cyclists**, factoring in not just distance, but also:

- 🌿 **Greenery** — how scenic and green the route feels
- 📐 **Slope** — how much effort is required to climb
- 🚦 **Maximum Speed** — how safe roads feel given traffic speeds
- ↩️ **Turning Left Cost** — how often uncomfortable left turns occur
- 📏 **Distance** — the total length of the route

Users set their own preference weights across these criteria, and the app calculates the optimal route using Dijkstra's algorithm on a custom-weighted graph.

---

## 🗂️ Repository Structure

```
GSDProject/
├── data/                   # Input datasets (OSM, DEM, cadastral, satellite)
├── greenery/               # Visibility-based and NDVI-based greenery analysis
├── graph/                  # Line graph construction and edge weight calculation
├── routing/                # Multi-criteria Dijkstra implementation
├── webapp/                 # Frontend web application
├── results/                # Sample route outputs and evaluation charts
└── README.md
```

---

## 🔍 Methodology

### 1. Greenery Criterion

Two complementary methods were developed to quantify how green each road segment is:

#### 🌳 Visibility-Based Analysis (Vector)
Using tree point data (OSM + Cadastral) and green area polygons (OSM Land Use):

- Each road edge is divided into waypoints spaced **≤ 5 m** apart.
- For each waypoint, visible trees are counted within a cone of **150° angle** and **200 m distance**.
- If the edge passes through a green area (forest, park, meadow, etc.), it is assigned the maximum greenery value automatically.
- The average tree count per waypoint is converted to a weight using an **exponential decay function**:

$$W_{Greenery} = \frac{\exp\!\left(-\frac{kx}{T}\right) - \exp(-k)}{1 - \exp(-k)}$$

where `k = 12` (cofactor) and `T = 548` (maximum tree count across all edges). Higher tree counts → lower weight → route is preferred.

#### 🛰️ NDVI-Based Analysis (Raster)
Using **Sentinel-2 satellite imagery** (10×10 m resolution):

- NDVI (Normalized Difference Vegetation Index) is calculated:

$$NDVI = \frac{NIR - Red}{NIR + Red}$$

- The road network is rasterized and overlaid with the NDVI image.
- **Zonal statistics** extract the mean NDVI value per road segment.
- This provides a complementary measure to visibility analysis — capturing roadside grass and broader green cover that may not contain visible trees.

**Key difference:** Visibility-based analysis measures what a cyclist actually *sees* (trees within line of sight). NDVI-based analysis measures overall greenery *present* in the area, including grass and shrubs.

---

### 2. Slope Criterion

Elevation data comes from a **1 m × 1 m Digital Elevation Model (DEM)**. Each graph node is assigned its closest DEM elevation value, and slope is computed per edge.

Slope weight is modeled as a piecewise function based on real cycling power data:

```
W_Slope = 0        if slope ≤ -3%
W_Slope = S(x)     if -3% < slope < 15%
W_Slope = 1        if slope ≥ 15%

where S(x) = sqrt(x + 3) / sqrt(18)
```

- Slopes ≤ −3% require no extra effort → weight = 0
- Slopes ≥ 15% are considered very painful for all riders → weight = 1 (maximum penalty)

---

### 3. Turning Left Cost

Left turns on a bike are effort-intensive and require crossing oncoming traffic. To model this, the road graph is converted into a **Line Graph** (where edges become nodes), which allows turn-specific costs to be expressed as edge weights:

$$W_{TurningLeft} = \begin{cases} 300 & \text{if turning left} \\ 0 & \text{otherwise} \end{cases}$$

---

### 4. Maximum Speed Criterion

Road speed limits are mapped to weights to penalize fast, potentially dangerous roads:

| Max Speed (km/h) | Weight |
|-----------------|--------|
| ≤ 10 or null | 0.00 |
| 10 – 30 | 0.25 |
| 30 – 50 | 0.50 |
| 50 – 70 | 0.75 |
| ≥ 70 | 1.00 |

---

### 5. Combined Edge Weight Formula

All criteria are combined into a single edge weight that Dijkstra's algorithm minimizes:

$$W = W_{Distance} \cdot \big(P_{Distance} + P_{Slope} \cdot W_{Slope} + P_{MaxSpeed} \cdot W_{MaxSpeed} + P_{Greenery} \cdot W_{Greenery}\big) + P_{TurningLeft} \cdot W_{TurningLeft}$$

Each `P_criteria` is a **user-defined preference value** from `{0, 1, 2, 3, 4}`. Preferences are normalized so they sum to 1. If all are set to 0, equal weights (0.2 each) are applied.

---

## 📊 Results

The application was evaluated over **1,000 randomly sampled source–destination pairs** across Bonn. Each pair was run with greenery weight set to 0, 1, 2, 3, 4, and 5 (greenest path only).

| Weight Index | Avg. Distance Change | Avg. Greenery Change |
|:---:|:---:|:---:|
| 0 (Shortest Path) | 100 | 100 |
| 1 | 103 | 127 |
| 2 | 107 | 140 |
| 3 | 109 | 150 |
| 4 | 112 | 158 |
| 5 (Greenest Path) | 174 | 248 |

**Key finding:** Cyclists can achieve significantly greener routes (up to +148% more greenery) by accepting only a modest detour — often less than 20% additional distance.

The visited road network covered **994.5 km** out of Bonn's total **1,881 km** of bike-accessible roads (52%).

---

## 🗄️ Data Sources

| Data | Type | Source |
|------|------|--------|
| Road network (bike paths) | Vector (edges/nodes) | OpenStreetMap via OSMNX |
| Green areas (parks, forests, meadows…) | Polygon | OpenStreetMap Land Use |
| Trees | Point | OpenStreetMap + Bonn Cadastral Data |
| Building footprints | Polygon | OpenStreetMap |
| Elevation | Raster (1 m) | Digital Elevation Model (DEM) |
| Satellite imagery | Raster (10 m) | Sentinel-2 |

---

## 🛠️ Tech Stack

- **Java** — core application (routing engine, graph construction, weight calculation)
- **Python** — supporting analysis (OSMnx, GeoPandas, NumPy, Rasterio)
- **JGraphT** — graph library for line graph construction and Dijkstra's algorithm
- **Maven** — dependency management and build tool
- **QGIS / GIS tools** — spatial visualization and zonal statistics
- **Web App** — frontend interface for interactive route planning

---

## 🚀 Getting Started

**Requirements:** Java 11+, Maven

```bash
git clone https://github.com/Sefahmet/GSDProject.git
cd GSDProject
```

Build the project:

```bash
mvn clean install
```

Run the application:

```bash
mvn exec:java -Dexec.mainClass="com.gsd.Main"
```

---

## 📚 References

- Dijkstra, E. W. (1959). *A Note on Two Problems in Connexion with Graphs.* Numerische Mathematik.
- Gedicke et al. (2019). *Selecting Landmarks for Wayfinding Assistance Based on Advance Visibility.* IJGIS.
- Gupta et al. (2012). *Urban Neighborhood Green Index.* Landscape and Urban Planning.
- Harary & Norman (1960). *Some Properties of Line Digraphs.* Rendiconti Del Circolo Matematico Di Palermo.
- Kriegler et al. (1969). *Preprocessing transformations and their effects on multispectral recognition.*
- Li et al. (2015). *Assessing street-level urban greenery using Google Street View.* Urban Forestry & Urban Greening.
- Parkin & Rotheram (2010). *Design speeds and acceleration characteristics of bicycle traffic.* Transport Policy.
- De Neef, M. (2013). *Gradients and Cycling: An Introduction.* theclimbingcyclist.com

---

## 📄 License

This project was developed as an academic Master's Project at the University of Bonn. Please contact the authors for usage permissions.
