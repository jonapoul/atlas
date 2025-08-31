## Task Dependencies

> [!NOTE]
> Downwards arrow == "depends on"

```mermaid
flowchart TD
  CalculateModuleTreeTask --> CollateModuleLinksTask
  CheckDotFileTask --> GenerateModulesDotFileTask
  CollateModuleTypesTask --> DumpModuleTypeTask
  CollateModuleLinksTask --> DumpModuleLinksTask
  GenerateModulesDotFileTask --> CalculateModuleTreeTask
  GenerateModulesDotFileTask --> CollateModuleTypesTask
  GeneratePngFileTask --> GenerateModulesDotFileTask
  GeneratePngFileTask --> GenerateLegendDotFileTask
```

For `DumpModuleLinksTask`, `DumpModuleTypeTask` and `CalculateModuleTreeTask`, one instance of each is registered for each of your smodules. All other tasks are attached on the root project. So your (filtered) task lists will look like below:

```
# Root project
calculateModuleTree
collateModuleLinks
collateModuleTypes
dumpModuleLinks
dumpModuleType
generateLegendDotFile

# Each subprojects
calculateModuleTree
dumpModuleLinks
dumpModuleType
```
