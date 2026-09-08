# Basic Sample Project

The same module layout as [sample-d2](../sample-d2), but with no Atlas configuration at all beyond
`d2 {}` - no project types, no link types, no styling. Every chart here is what Atlas produces with
its defaults.

The modules are plain Java ones. Their names mirror the other samples for familiarity, but since
this sample declares no project types, the plugin applied in each makes no difference to the charts.

`scripts/runAtlasGenerate.sh` renders [`android/app`](android/app)'s chart once per layout engine to
produce the comparison images in `docs/docs/img/d2-layoutEngine-*.svg`, so regenerate the samples
after changing anything here.
