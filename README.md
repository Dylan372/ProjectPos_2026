# ProjectPos_2026 — PoS System Using Agile

Software Engineering Practice (毕业实习) — a simplified supermarket **point-of-sale (PoS) system**
with two core use cases: **Process Sale** and **Handle Returns**, developed with **Scrum**.

## Team & Collaboration

- Maintainer: Jindong Wang
- Collaborators: Jindong Wang / Peilin He / Tianci Zeng
- Each member works on a unique branch (`member-A`, `member-B`, `member-C`),
  merges into `main` via Pull Requests. **Branches are kept after merging.**
- Repository: https://github.com/Dylan372/ProjectPos_2026

## Repository layout

```
ProjectPos_2026/
├── src/pos/          Java sources (package pos)
├── data/             products.txt, inventory.txt
├── compile.bat       compile (JDK 17+)
├── run.bat           interactive console
├── run-demo.bat      scripted end-to-end demo (for the video)
└── test.bat          automated test suite
```

## Build & run

```bat
compile.bat     rem javac -encoding UTF-8 -d out src\pos\*.java
run.bat         rem interactive
run-demo.bat    rem scripted demo
test.bat        rem unit tests (8 test groups)
```

## Architecture (see Tech Report for diagrams)

- **Presentation**: `ConsoleUI` (Model-View separation — no business logic)
- **Application**: `Register` (GRASP Controller — thin coordinator, delegates)
- **Domain**: `Sale`, `SalesLineItem`, `Payment`, `Return`, `ReturnLineItem`,
  `ProductSpecification` (Information Expert, Creator)
- **Infrastructure**: `ProductCatalog` / `Inventory` file loading and persistence

System operations mirror the SSDs of the use cases:
`makeNewSale()`, `enterItem(id, qty)`, `endSale()`, `makePayment(amount)`,
`makeNewReturn()`, `enterReturnedItem(id, qty)`, `endReturn()`, `receiveRefund(amount)`.
