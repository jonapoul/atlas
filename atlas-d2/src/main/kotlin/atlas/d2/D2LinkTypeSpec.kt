package atlas.d2

import atlas.core.LinkTypeSpec

/**
 * Style specs from [the D2 docs](https://d2lang.com/tour/style), applied to the link between two
 * modules. See [D2PropertiesSpec] for the available options, shared between links and modules.
 */
public interface D2LinkTypeSpec : LinkTypeSpec, D2PropertiesSpec
