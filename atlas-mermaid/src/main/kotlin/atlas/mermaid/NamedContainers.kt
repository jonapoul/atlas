package atlas.mermaid

import atlas.core.AtlasDsl
import atlas.core.NamedLinkTypeContainer
import atlas.core.NamedProjectTypeContainer

@AtlasDsl
public interface MermaidNamedLinkTypeContainer : NamedLinkTypeContainer<MermaidLinkTypeSpec>

@AtlasDsl
public interface MermaidNamedProjectTypeContainer : NamedProjectTypeContainer<MermaidProjectTypeSpec>
