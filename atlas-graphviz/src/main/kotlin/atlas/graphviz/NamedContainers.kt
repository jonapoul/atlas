package atlas.graphviz

import atlas.core.AtlasDsl
import atlas.core.NamedLinkTypeContainer
import atlas.core.NamedModuleTypeContainer

@AtlasDsl
public interface GraphvizNamedLinkTypeContainer : NamedLinkTypeContainer<GraphvizLinkTypeSpec>

@AtlasDsl
public interface GraphvizNamedModuleTypeContainer : NamedModuleTypeContainer<GraphvizModuleTypeSpec>
