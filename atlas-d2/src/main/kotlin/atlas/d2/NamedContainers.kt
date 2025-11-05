package atlas.d2

import atlas.core.AtlasDsl
import atlas.core.NamedLinkTypeContainer
import atlas.core.NamedProjectTypeContainer

@AtlasDsl
public interface D2NamedLinkTypeContainer : NamedLinkTypeContainer<D2LinkTypeSpec>

@AtlasDsl
public interface D2NamedProjectTypeContainer : NamedProjectTypeContainer<D2ProjectTypeSpec>
