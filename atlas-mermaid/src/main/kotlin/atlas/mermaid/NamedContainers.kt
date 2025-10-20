package atlas.mermaid

import atlas.core.AtlasDsl
import atlas.core.NamedLinkTypeContainer
import atlas.core.NamedModuleTypeContainer

@AtlasDsl
public interface MermaidNamedLinkTypeContainer : NamedLinkTypeContainer<MermaidLinkTypeSpec>

@AtlasDsl
public interface MermaidNamedModuleTypeContainer : NamedModuleTypeContainer<MermaidModuleTypeSpec>
