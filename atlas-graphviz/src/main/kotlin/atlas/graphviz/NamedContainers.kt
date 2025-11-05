package atlas.graphviz

import atlas.core.AtlasDsl
import atlas.core.NamedLinkTypeContainer
import atlas.core.NamedProjectTypeContainer

@AtlasDsl
public interface GraphvizNamedLinkTypeContainer : NamedLinkTypeContainer<GraphvizLinkTypeSpec>

@AtlasDsl
public interface GraphvizNamedProjectTypeContainer : NamedProjectTypeContainer<GraphvizProjectTypeSpec>
