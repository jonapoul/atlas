/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("SpellCheckingInspection")

package atlas.graphviz

import atlas.core.AtlasDsl
import atlas.core.PropertiesSpec

/**
 * These attributes will be applied by default to all module nodes, unless overridden by that node's
 * [GraphvizModuleTypeSpec].
 *
 * See [the Graphviz docs](https://graphviz.org/docs/nodes/) for any restrictions, this interface will
 * just pass-through the attribute and let Graphviz handle validation.
 */
@AtlasDsl
public interface NodeAttributes : PropertiesSpec {
  /**
   * [https://graphviz.org/docs/attrs/color/](https://graphviz.org/docs/attrs/color/)
   */
  public var lineColor: String?

  /**
   * [https://graphviz.org/docs/attrs/colorscheme/](https://graphviz.org/docs/attrs/colorscheme/)
   */
  public var colorScheme: String?

  /**
   * [https://graphviz.org/docs/attrs/comment/](https://graphviz.org/docs/attrs/comment/)
   */
  public var comment: String?

  /**
   * [https://graphviz.org/docs/attrs/distortion/](https://graphviz.org/docs/attrs/distortion/)
   */
  public var distortion: String?

  /**
   * [https://graphviz.org/docs/attrs/fillcolor/](https://graphviz.org/docs/attrs/fillcolor/)
   */
  public var fillColor: String?

  /**
   * [https://graphviz.org/docs/attrs/fixedsize/](https://graphviz.org/docs/attrs/fixedsize/)
   */
  public var fixedSize: String?

  /**
   * [https://graphviz.org/docs/attrs/fontcolor/](https://graphviz.org/docs/attrs/fontcolor/)
   */
  public var fontColor: String?

  /**
   * [https://graphviz.org/docs/attrs/fontname/](https://graphviz.org/docs/attrs/fontname/)
   */
  public var fontName: String?

  /**
   * [https://graphviz.org/docs/attrs/fontsize/](https://graphviz.org/docs/attrs/fontsize/)
   */
  public var fontSize: String?

  /**
   * [https://graphviz.org/docs/attrs/gradientangle/](https://graphviz.org/docs/attrs/gradientangle/)
   */
  public var gradientAngle: Int?

  /**
   * [https://graphviz.org/docs/attrs/group/](https://graphviz.org/docs/attrs/group/)
   */
  public var group: String?

  /**
   * [https://graphviz.org/docs/attrs/height/](https://graphviz.org/docs/attrs/height/)
   */
  public var height: Number?

  /**
   * [https://graphviz.org/docs/attrs/href/](https://graphviz.org/docs/attrs/href/)
   */
  public var href: String?

  /**
   * [https://graphviz.org/docs/attrs/id/](https://graphviz.org/docs/attrs/id/)
   */
  public var id: String?

  /**
   * [https://graphviz.org/docs/attrs/image/](https://graphviz.org/docs/attrs/image/)
   */
  public var image: String?

  /**
   * [https://graphviz.org/docs/attrs/imagepos/](https://graphviz.org/docs/attrs/imagepos/)
   */
  public var imagePos: ImagePos?

  /**
   * [https://graphviz.org/docs/attrs/imagescale/](https://graphviz.org/docs/attrs/imagescale/)
   */
  public var imageScale: String?

  /**
   * [https://graphviz.org/docs/attrs/label/](https://graphviz.org/docs/attrs/label/)
   */
  public var label: String?

  /**
   * [https://graphviz.org/docs/attrs/labelloc/](https://graphviz.org/docs/attrs/labelloc/)
   */
  public var labelLoc: String?

  /**
   * [https://graphviz.org/docs/attrs/layer/](https://graphviz.org/docs/attrs/layer/)
   */
  public var layer: String?

  /**
   * [https://graphviz.org/docs/attrs/margin/](https://graphviz.org/docs/attrs/margin/)
   */
  public var margin: String?

  /**
   * [https://graphviz.org/docs/attrs/nojustify/](https://graphviz.org/docs/attrs/nojustify/)
   */
  public var noJustify: Boolean?

  /**
   * [https://graphviz.org/docs/attrs/ordering/](https://graphviz.org/docs/attrs/ordering/)
   */
  public var ordering: String?

  /**
   * [https://graphviz.org/docs/attrs/orientation/](https://graphviz.org/docs/attrs/orientation/)
   */
  public var orientation: Number?

  /**
   * [https://graphviz.org/docs/attrs/penwidth/](https://graphviz.org/docs/attrs/penwidth/)
   */
  public var penWidth: Number?

  /**
   * [https://graphviz.org/docs/attrs/peripheries/](https://graphviz.org/docs/attrs/peripheries/)
   */
  public var peripheries: Int?

  /**
   * [https://graphviz.org/docs/attrs/pin/](https://graphviz.org/docs/attrs/pin/)
   */
  public var pin: Boolean?

  /**
   * [https://graphviz.org/docs/attrs/pos/](https://graphviz.org/docs/attrs/pos/)
   */
  public var pos: String?

  /**
   * [https://graphviz.org/docs/attrs/rects/](https://graphviz.org/docs/attrs/rects/)
   */
  public var rects: String?

  /**
   * [https://graphviz.org/docs/attrs/regular/](https://graphviz.org/docs/attrs/regular/)
   */
  public var regular: Boolean?

  /**
   * [https://graphviz.org/docs/attrs/root/](https://graphviz.org/docs/attrs/root/)
   */
  public var root: String?

  /**
   * [https://graphviz.org/docs/attrs/samplepoints/](https://graphviz.org/docs/attrs/samplepoints/)
   */
  public var samplePoints: Int?

  /**
   * [https://graphviz.org/docs/attrs/shape/](https://graphviz.org/docs/attrs/shape/)
   */
  public var shape: Shape?

  /**
   * [https://graphviz.org/docs/attrs/shapefile/](https://graphviz.org/docs/attrs/shapefile/)
   */
  public var shapeFile: String?

  /**
   * [https://graphviz.org/docs/attrs/showboxes/](https://graphviz.org/docs/attrs/showboxes/)
   */
  public var showBoxes: Int?

  /**
   * [https://graphviz.org/docs/attrs/sides/](https://graphviz.org/docs/attrs/sides/)
   */
  public var sides: Int?

  /**
   * [https://graphviz.org/docs/attrs/skew/](https://graphviz.org/docs/attrs/skew/)
   */
  public var skew: Number?

  /**
   * [https://graphviz.org/docs/attrs/sortv/](https://graphviz.org/docs/attrs/sortv/)
   */
  public var sortv: Int?

  /**
   * [https://graphviz.org/docs/attrs/style/](https://graphviz.org/docs/attrs/style/)
   */
  public var style: NodeStyle?

  /**
   * [https://graphviz.org/docs/attrs/target/](https://graphviz.org/docs/attrs/target/)
   */
  public var target: String?

  /**
   * [https://graphviz.org/docs/attrs/tooltip/](https://graphviz.org/docs/attrs/tooltip/)
   */
  public var tooltip: String?

  /**
   * [https://graphviz.org/docs/attrs/URL/](https://graphviz.org/docs/attrs/URL/)
   */
  public var url: String?

  /**
   * [https://graphviz.org/docs/attrs/vertices/](https://graphviz.org/docs/attrs/vertices/)
   */
  public var vertices: String?

  /**
   * [https://graphviz.org/docs/attrs/width/](https://graphviz.org/docs/attrs/width/)
   */
  public var width: Number?

  /**
   * [https://graphviz.org/docs/attrs/xlabel/](https://graphviz.org/docs/attrs/xlabel/)
   */
  public var xlabel: String?

  /**
   * [https://graphviz.org/docs/attrs/xlp/](https://graphviz.org/docs/attrs/xlp/)
   */
  public var xlp: String?

  /**
   * [https://graphviz.org/docs/attrs/z/](https://graphviz.org/docs/attrs/z/)
   */
  public var z: Number?
}

/**
 * These attributes will be applied by default to all module edges (AKA link lines), unless overridden by that link's
 * [GraphvizLinkTypeSpec].
 *
 * See [the Graphviz docs](https://graphviz.org/docs/edges/) for any restrictions, this interface will
 * just pass-through the attribute and let Graphviz handle validation.
 *
 * [linkColor] and [linkStyle] have been renamed from "color" and "style" to avoid clashes with
 * [atlas.core.LinkTypeSpec] attributes.
 */
@AtlasDsl
public interface EdgeAttributes : PropertiesSpec {
  /**
   * [https://graphviz.org/docs/attrs/arrowhead/](https://graphviz.org/docs/attrs/arrowhead/)
   */
  public var arrowHead: ArrowType?

  /**
   * [https://graphviz.org/docs/attrs/arrowsize/](https://graphviz.org/docs/attrs/arrowsize/)
   */
  public var arrowSize: Number?

  /**
   * [https://graphviz.org/docs/attrs/arrowtail/](https://graphviz.org/docs/attrs/arrowtail/)
   */
  public var arrowTail: ArrowType?

  /**
   * [https://graphviz.org/docs/attrs/color/](https://graphviz.org/docs/attrs/color/)
   */
  public var linkColor: String?

  /**
   * [https://graphviz.org/docs/attrs/colorscheme/](https://graphviz.org/docs/attrs/colorscheme/)
   */
  public var colorScheme: String?

  /**
   * [https://graphviz.org/docs/attrs/comment/](https://graphviz.org/docs/attrs/comment/)
   */
  public var comment: String?

  /**
   * [https://graphviz.org/docs/attrs/constraint/](https://graphviz.org/docs/attrs/constraint/)
   */
  public var constraint: Boolean?

  /**
   * [https://graphviz.org/docs/attrs/decorate/](https://graphviz.org/docs/attrs/decorate/)
   */
  public var decorate: Boolean?

  /**
   * [https://graphviz.org/docs/attrs/dir/](https://graphviz.org/docs/attrs/dir/)
   */
  public var dir: Dir?

  /**
   * [https://graphviz.org/docs/attrs/edgehref/](https://graphviz.org/docs/attrs/edgehref/)
   */
  public var edgeHref: String?

  /**
   * [https://graphviz.org/docs/attrs/edgetarget/](https://graphviz.org/docs/attrs/edgetarget/)
   */
  public var edgeTarget: String?

  /**
   * [https://graphviz.org/docs/attrs/edgetooltip/](https://graphviz.org/docs/attrs/edgetooltip/)
   */
  public var edgeTooltip: String?

  /**
   * [https://graphviz.org/docs/attrs/edgeURL/](https://graphviz.org/docs/attrs/edgeURL/)
   */
  public var edgeUrl: String?

  /**
   * [https://graphviz.org/docs/attrs/fillcolor/](https://graphviz.org/docs/attrs/fillcolor/)
   */
  public var fillColor: String?

  /**
   * [https://graphviz.org/docs/attrs/fontcolor/](https://graphviz.org/docs/attrs/fontcolor/)
   */
  public var fontColor: String?

  /**
   * [https://graphviz.org/docs/attrs/fontname/](https://graphviz.org/docs/attrs/fontname/)
   */
  public var fontName: String?

  /**
   * [https://graphviz.org/docs/attrs/fontsize/](https://graphviz.org/docs/attrs/fontsize/)
   */
  public var fontSize: String?

  /**
   * [https://graphviz.org/docs/attrs/head_lp/](https://graphviz.org/docs/attrs/head_lp/)
   */
  public var headLp: String?

  /**
   * [https://graphviz.org/docs/attrs/headclip/](https://graphviz.org/docs/attrs/headclip/)
   */
  public var headClip: Boolean?

  /**
   * [https://graphviz.org/docs/attrs/headhref/](https://graphviz.org/docs/attrs/headhref/)
   */
  public var headHref: String?

  /**
   * [https://graphviz.org/docs/attrs/headlabel/](https://graphviz.org/docs/attrs/headlabel/)
   */
  public var headLabel: String?

  /**
   * [https://graphviz.org/docs/attrs/headport/](https://graphviz.org/docs/attrs/headport/)
   */
  public var headPort: String?

  /**
   * [https://graphviz.org/docs/attrs/headtarget/](https://graphviz.org/docs/attrs/headtarget/)
   */
  public var headTarget: String?

  /**
   * [https://graphviz.org/docs/attrs/headtooltip/](https://graphviz.org/docs/attrs/headtooltip/)
   */
  public var headTooltip: String?

  /**
   * [https://graphviz.org/docs/attrs/headURL/](https://graphviz.org/docs/attrs/headURL/)
   */
  public var headUrl: String?

  /**
   * [https://graphviz.org/docs/attrs/href/](https://graphviz.org/docs/attrs/href/)
   */
  public var href: String?

  /**
   * [https://graphviz.org/docs/attrs/id/](https://graphviz.org/docs/attrs/id/)
   */
  public var id: String?

  /**
   * [https://graphviz.org/docs/attrs/label/](https://graphviz.org/docs/attrs/label/)
   */
  public var label: String?

  /**
   * [https://graphviz.org/docs/attrs/labelangle/](https://graphviz.org/docs/attrs/labelangle/)
   */
  public var labelAngle: Number?

  /**
   * [https://graphviz.org/docs/attrs/labeldistance/](https://graphviz.org/docs/attrs/labeldistance/)
   */
  public var labelDistance: Number?

  /**
   * [https://graphviz.org/docs/attrs/labelfloat/](https://graphviz.org/docs/attrs/labelfloat/)
   */
  public var labelFloat: Boolean?

  /**
   * [https://graphviz.org/docs/attrs/labelfontcolor/](https://graphviz.org/docs/attrs/labelfontcolor/)
   */
  public var labelFontColor: String?

  /**
   * [https://graphviz.org/docs/attrs/labelfontname/](https://graphviz.org/docs/attrs/labelfontname/)
   */
  public var labelFontName: String?

  /**
   * [https://graphviz.org/docs/attrs/labelfontsize/](https://graphviz.org/docs/attrs/labelfontsize/)
   */
  public var labelFontSize: String?

  /**
   * [https://graphviz.org/docs/attrs/labelhref/](https://graphviz.org/docs/attrs/labelhref/)
   */
  public var labelHref: String?

  /**
   * [https://graphviz.org/docs/attrs/labeltarget/](https://graphviz.org/docs/attrs/labeltarget/)
   */
  public var labelTarget: String?

  /**
   * [https://graphviz.org/docs/attrs/labeltooltip/](https://graphviz.org/docs/attrs/labeltooltip/)
   */
  public var labelTooltip: String?

  /**
   * [https://graphviz.org/docs/attrs/labelurl/](https://graphviz.org/docs/attrs/labelurl/)
   */
  public var labelUrl: String?

  /**
   * [https://graphviz.org/docs/attrs/layer/](https://graphviz.org/docs/attrs/layer/)
   */
  public var layer: String?

  /**
   * [https://graphviz.org/docs/attrs/len/](https://graphviz.org/docs/attrs/len/)
   */
  public var len: Number?

  /**
   * [https://graphviz.org/docs/attrs/lhead/](https://graphviz.org/docs/attrs/lhead/)
   */
  public var lhead: String?

  /**
   * [https://graphviz.org/docs/attrs/lp/](https://graphviz.org/docs/attrs/lp/)
   */
  public var lp: String?

  /**
   * [https://graphviz.org/docs/attrs/ltail/](https://graphviz.org/docs/attrs/ltail/)
   */
  public var ltail: String?

  /**
   * [https://graphviz.org/docs/attrs/minlen/](https://graphviz.org/docs/attrs/minlen/)
   */
  public var minLen: Int?

  /**
   * [https://graphviz.org/docs/attrs/nojustify/](https://graphviz.org/docs/attrs/nojustify/)
   */
  public var noJustify: Boolean?

  /**
   * [https://graphviz.org/docs/attrs/penwidth/](https://graphviz.org/docs/attrs/penwidth/)
   */
  public var penWidth: Number?

  /**
   * [https://graphviz.org/docs/attrs/pos/](https://graphviz.org/docs/attrs/pos/)
   */
  public var pos: String?

  /**
   * [https://graphviz.org/docs/attrs/samehead/](https://graphviz.org/docs/attrs/samehead/)
   */
  public var sameHead: String?

  /**
   * [https://graphviz.org/docs/attrs/sametail/](https://graphviz.org/docs/attrs/sametail/)
   */
  public var sameTail: String?

  /**
   * [https://graphviz.org/docs/attrs/showboxes/](https://graphviz.org/docs/attrs/showboxes/)
   */
  public var showBoxes: Int?

  /**
   * [https://graphviz.org/docs/attrs/style/](https://graphviz.org/docs/attrs/style/)
   */
  public var linkStyle: LinkStyle?

  /**
   * [https://graphviz.org/docs/attrs/tail_lp/](https://graphviz.org/docs/attrs/tail_lp/)
   */
  public var tailLp: String?

  /**
   * [https://graphviz.org/docs/attrs/tailclip/](https://graphviz.org/docs/attrs/tailclip/)
   */
  public var tailClip: Boolean?

  /**
   * [https://graphviz.org/docs/attrs/tailhref/](https://graphviz.org/docs/attrs/tailhref/)
   */
  public var tailHref: String?

  /**
   * [https://graphviz.org/docs/attrs/taillabel/](https://graphviz.org/docs/attrs/taillabel/)
   */
  public var tailLabel: String?

  /**
   * [https://graphviz.org/docs/attrs/tailport/](https://graphviz.org/docs/attrs/tailport/)
   */
  public var tailPort: String?

  /**
   * [https://graphviz.org/docs/attrs/tailtarget/](https://graphviz.org/docs/attrs/tailtarget/)
   */
  public var tailTarget: String?

  /**
   * [https://graphviz.org/docs/attrs/tailtooltip/](https://graphviz.org/docs/attrs/tailtooltip/)
   */
  public var tailTooltip: String?

  /**
   * [https://graphviz.org/docs/attrs/tailURL/](https://graphviz.org/docs/attrs/tailURL/)
   */
  public var tailUrl: String?

  /**
   * [https://graphviz.org/docs/attrs/target/](https://graphviz.org/docs/attrs/target/)
   */
  public var target: String?

  /**
   * [https://graphviz.org/docs/attrs/tooltip/](https://graphviz.org/docs/attrs/tooltip/)
   */
  public var tooltip: String?

  /**
   * [https://graphviz.org/docs/attrs/url/](https://graphviz.org/docs/attrs/url/)
   */
  public var url: String?

  /**
   * [https://graphviz.org/docs/attrs/weight/](https://graphviz.org/docs/attrs/weight/)
   */
  public var weight: Number?

  /**
   * [https://graphviz.org/docs/attrs/xlabel/](https://graphviz.org/docs/attrs/xlabel/)
   */
  public var xLabel: String?

  /**
   * [https://graphviz.org/docs/attrs/xlp/](https://graphviz.org/docs/attrs/xlp/)
   */
  public var xlp: String?
}

/**
 * These attributes will be applied to the root graph area.
 *
 * See [https://graphviz.org/docs/graph/](https://graphviz.org/docs/graph/) for any restrictions, this interface will
 * just pass-through the attribute and let Graphviz handle validation.
 */
@AtlasDsl
public interface GraphAttributes : PropertiesSpec {
  /**
   * [https://graphviz.org/docs/attrs/background/](https://graphviz.org/docs/attrs/background/)
   */
  public var background: String?

  /**
   * [https://graphviz.org/docs/attrs/bb/](https://graphviz.org/docs/attrs/bb/)
   */
  public var bb: String?

  /**
   * [https://graphviz.org/docs/attrs/beautify/](https://graphviz.org/docs/attrs/beautify/)
   */
  public var beautify: Boolean?

  /**
   * [https://graphviz.org/docs/attrs/bgcolor/](https://graphviz.org/docs/attrs/bgcolor/)
   */
  public var bgColor: String?

  /**
   * [https://graphviz.org/docs/attrs/center/](https://graphviz.org/docs/attrs/center/)
   */
  public var center: Boolean?

  /**
   * [https://graphviz.org/docs/attrs/charset/](https://graphviz.org/docs/attrs/charset/)
   */
  public var charset: String?

  /**
   * [https://graphviz.org/docs/attrs/clusterrank/](https://graphviz.org/docs/attrs/clusterrank/)
   */
  public var clusterRank: ClusterMode?

  /**
   * [https://graphviz.org/docs/attrs/colorscheme/](https://graphviz.org/docs/attrs/colorscheme/)
   */
  public var colorScheme: String?

  /**
   * [https://graphviz.org/docs/attrs/comment/](https://graphviz.org/docs/attrs/comment/)
   */
  public var comment: String?

  /**
   * [https://graphviz.org/docs/attrs/compound/](https://graphviz.org/docs/attrs/compound/)
   */
  public var compound: Boolean?

  /**
   * [https://graphviz.org/docs/attrs/concentrate/](https://graphviz.org/docs/attrs/concentrate/)
   */
  public var concentrate: Boolean?

  /**
   * [https://graphviz.org/docs/attrs/Damping/](https://graphviz.org/docs/attrs/Damping/)
   */
  public var damping: Float?

  /**
   * [https://graphviz.org/docs/attrs/defaultdist/](https://graphviz.org/docs/attrs/defaultdist/)
   */
  public var defaultDist: Number?

  /**
   * [https://graphviz.org/docs/attrs/dim/](https://graphviz.org/docs/attrs/dim/)
   */
  public var dim: Int?

  /**
   * [https://graphviz.org/docs/attrs/dimen/](https://graphviz.org/docs/attrs/dimen/)
   */
  public var dimen: Int?

  /**
   * [https://graphviz.org/docs/attrs/diredgeconstraints/](https://graphviz.org/docs/attrs/diredgeconstraints/)
   */
  public var dirEdgeConstraints: String?

  /**
   * [https://graphviz.org/docs/attrs/dpi/](https://graphviz.org/docs/attrs/dpi/)
   */
  public var dpi: Number?

  /**
   * [https://graphviz.org/docs/attrs/epsilon/](https://graphviz.org/docs/attrs/epsilon/)
   */
  public var epsilon: Float?

  /**
   * [https://graphviz.org/docs/attrs/esep/](https://graphviz.org/docs/attrs/esep/)
   */
  public var esep: String?

  /**
   * [https://graphviz.org/docs/attrs/fontcolor/](https://graphviz.org/docs/attrs/fontcolor/)
   */
  public var fontColor: String?

  /**
   * [https://graphviz.org/docs/attrs/fontname/](https://graphviz.org/docs/attrs/fontname/)
   */
  public var fontName: String?

  /**
   * [https://graphviz.org/docs/attrs/fontnames/](https://graphviz.org/docs/attrs/fontnames/)
   */
  public var fontNames: String?

  /**
   * [https://graphviz.org/docs/attrs/fontpath/](https://graphviz.org/docs/attrs/fontpath/)
   */
  public var fontPath: String?

  /**
   * [https://graphviz.org/docs/attrs/fontsize/](https://graphviz.org/docs/attrs/fontsize/)
   */
  public var fontSize: String?

  /**
   * [https://graphviz.org/docs/attrs/forcelabels/](https://graphviz.org/docs/attrs/forcelabels/)
   */
  public var forceLabels: Boolean?

  /**
   * [https://graphviz.org/docs/attrs/gradientangle/](https://graphviz.org/docs/attrs/gradientangle/)
   */
  public var gradientAngle: Int?

  /**
   * [https://graphviz.org/docs/attrs/href/](https://graphviz.org/docs/attrs/href/)
   */
  public var href: String?

  /**
   * [https://graphviz.org/docs/attrs/id/](https://graphviz.org/docs/attrs/id/)
   */
  public var id: String?

  /**
   * [https://graphviz.org/docs/attrs/imagepath/](https://graphviz.org/docs/attrs/imagepath/)
   */
  public var imagePath: String?

  /**
   * [https://graphviz.org/docs/attrs/inputscale/](https://graphviz.org/docs/attrs/inputscale/)
   */
  public var inputScale: Number?

  /**
   * [https://graphviz.org/docs/attrs/K/](https://graphviz.org/docs/attrs/K/)
   */
  public var k: Number?

  /**
   * [https://graphviz.org/docs/attrs/label/](https://graphviz.org/docs/attrs/label/)
   */
  public var label: String?

  /**
   * [https://graphviz.org/docs/attrs/label_scheme/](https://graphviz.org/docs/attrs/label_scheme/)
   */
  public var labelScheme: Int?

  /**
   * [https://graphviz.org/docs/attrs/labeljust/](https://graphviz.org/docs/attrs/labeljust/)
   */
  public var labelJust: String?

  /**
   * [https://graphviz.org/docs/attrs/labelloc/](https://graphviz.org/docs/attrs/labelloc/)
   */
  public var labelLoc: String?

  /**
   * [https://graphviz.org/docs/attrs/landscape/](https://graphviz.org/docs/attrs/landscape/)
   */
  public var landscape: Boolean?

  /**
   * [https://graphviz.org/docs/attrs/layerlistsep/](https://graphviz.org/docs/attrs/layerlistsep/)
   */
  public var layerListSep: String?

  /**
   * [https://graphviz.org/docs/attrs/layers/](https://graphviz.org/docs/attrs/layers/)
   */
  public var layers: String?

  /**
   * [https://graphviz.org/docs/attrs/layerselect/](https://graphviz.org/docs/attrs/layerselect/)
   */
  public var layerSelect: String?

  /**
   * [https://graphviz.org/docs/attrs/layersep/](https://graphviz.org/docs/attrs/layersep/)
   */
  public var layerSep: String?

  /**
   * [https://graphviz.org/docs/attrs/layout/](https://graphviz.org/docs/attrs/layout/)
   */
  public var layout: LayoutEngine?

  /**
   * [https://graphviz.org/docs/attrs/levels/](https://graphviz.org/docs/attrs/levels/)
   */
  public var levels: Int?

  /**
   * [https://graphviz.org/docs/attrs/levelsgap/](https://graphviz.org/docs/attrs/levelsgap/)
   */
  public var levelsGap: Number?

  /**
   * [https://graphviz.org/docs/attrs/lheight/](https://graphviz.org/docs/attrs/lheight/)
   */
  public var lheight: Number?

  /**
   * [https://graphviz.org/docs/attrs/linelength/](https://graphviz.org/docs/attrs/linelength/)
   */
  public var lineLength: Int?

  /**
   * [https://graphviz.org/docs/attrs/lp/](https://graphviz.org/docs/attrs/lp/)
   */
  public var lp: String?

  /**
   * [https://graphviz.org/docs/attrs/lwidth/](https://graphviz.org/docs/attrs/lwidth/)
   */
  public var lWidth: Number?

  /**
   * [https://graphviz.org/docs/attrs/margin/](https://graphviz.org/docs/attrs/margin/)
   */
  public var margin: String?

  /**
   * [https://graphviz.org/docs/attrs/maxiter/](https://graphviz.org/docs/attrs/maxiter/)
   */
  public var maxiter: Int?

  /**
   * [https://graphviz.org/docs/attrs/mclimit/](https://graphviz.org/docs/attrs/mclimit/)
   */
  public var mcLimit: Number?

  /**
   * [https://graphviz.org/docs/attrs/mindist/](https://graphviz.org/docs/attrs/mindist/)
   */
  public var minDist: Number?

  /**
   * [https://graphviz.org/docs/attrs/mode/](https://graphviz.org/docs/attrs/mode/)
   */
  public var mode: String?

  /**
   * [https://graphviz.org/docs/attrs/model/](https://graphviz.org/docs/attrs/model/)
   */
  public var model: String?

  /**
   * [https://graphviz.org/docs/attrs/newrank/](https://graphviz.org/docs/attrs/newrank/)
   */
  public var newRank: Boolean?

  /**
   * [https://graphviz.org/docs/attrs/nodesep/](https://graphviz.org/docs/attrs/nodesep/)
   */
  public var nodeSep: Number?

  /**
   * [https://graphviz.org/docs/attrs/nojustify/](https://graphviz.org/docs/attrs/nojustify/)
   */
  public var noJustify: Boolean?

  /**
   * [https://graphviz.org/docs/attrs/normalize/](https://graphviz.org/docs/attrs/normalize/)
   */
  public var normalize: String?

  /**
   * [https://graphviz.org/docs/attrs/notranslate/](https://graphviz.org/docs/attrs/notranslate/)
   */
  public var noTranslate: Boolean?

  /**
   * [https://graphviz.org/docs/attrs/nslimit/](https://graphviz.org/docs/attrs/nslimit/)
   */
  public var nsLimit: Number?

  /**
   * [https://graphviz.org/docs/attrs/nslimit1/](https://graphviz.org/docs/attrs/nslimit1/)
   */
  public var nsLimit1: Number?

  /**
   * [https://graphviz.org/docs/attrs/oneblock/](https://graphviz.org/docs/attrs/oneblock/)
   */
  public var oneBlock: Boolean?

  /**
   * [https://graphviz.org/docs/attrs/ordering/](https://graphviz.org/docs/attrs/ordering/)
   */
  public var ordering: String?

  /**
   * [https://graphviz.org/docs/attrs/orientation/](https://graphviz.org/docs/attrs/orientation/)
   */
  public var orientation: Number?

  /**
   * [https://graphviz.org/docs/attrs/outputorder/](https://graphviz.org/docs/attrs/outputorder/)
   */
  public var outputOrder: String?

  /**
   * [https://graphviz.org/docs/attrs/overlap/](https://graphviz.org/docs/attrs/overlap/)
   */
  public var overlap: String?

  /**
   * [https://graphviz.org/docs/attrs/overlap_scaling/](https://graphviz.org/docs/attrs/overlap_scaling/)
   */
  public var overlapScaling: Number?

  /**
   * [https://graphviz.org/docs/attrs/overlap_shrink/](https://graphviz.org/docs/attrs/overlap_shrink/)
   */
  public var overlapShrink: Boolean?

  /**
   * [https://graphviz.org/docs/attrs/pack/](https://graphviz.org/docs/attrs/pack/)
   */
  public var pack: String?

  /**
   * [https://graphviz.org/docs/attrs/packmode/](https://graphviz.org/docs/attrs/packmode/)
   */
  public var packMode: String?

  /**
   * [https://graphviz.org/docs/attrs/pad/](https://graphviz.org/docs/attrs/pad/)
   */
  public var pad: String?

  /**
   * [https://graphviz.org/docs/attrs/page/](https://graphviz.org/docs/attrs/page/)
   */
  public var page: String?

  /**
   * [https://graphviz.org/docs/attrs/pagedir/](https://graphviz.org/docs/attrs/pagedir/)
   */
  public var pageDir: String?

  /**
   * [https://graphviz.org/docs/attrs/quadtree/](https://graphviz.org/docs/attrs/quadtree/)
   */
  public var quadTree: String?

  /**
   * [https://graphviz.org/docs/attrs/quantum/](https://graphviz.org/docs/attrs/quantum/)
   */
  public var quantum: Number?

  /**
   * [https://graphviz.org/docs/attrs/rankdir/](https://graphviz.org/docs/attrs/rankdir/)
   */
  public var rankDir: RankDir?

  /**
   * [https://graphviz.org/docs/attrs/ranksep/](https://graphviz.org/docs/attrs/ranksep/)
   */
  public var rankSep: Number?

  /**
   * [https://graphviz.org/docs/attrs/ratio/](https://graphviz.org/docs/attrs/ratio/)
   */
  public var ratio: String?

  /**
   * [https://graphviz.org/docs/attrs/remincross/](https://graphviz.org/docs/attrs/remincross/)
   */
  public var reminCross: Boolean?

  /**
   * [https://graphviz.org/docs/attrs/repulsiveforce/](https://graphviz.org/docs/attrs/repulsiveforce/)
   */
  public var repulsiveForce: Number?

  /**
   * [https://graphviz.org/docs/attrs/resolution/](https://graphviz.org/docs/attrs/resolution/)
   */
  public var resolution: Number?

  /**
   * [https://graphviz.org/docs/attrs/root/](https://graphviz.org/docs/attrs/root/)
   */
  public var root: String?

  /**
   * [https://graphviz.org/docs/attrs/rotate/](https://graphviz.org/docs/attrs/rotate/)
   */
  public var rotate: Int?

  /**
   * [https://graphviz.org/docs/attrs/rotation/](https://graphviz.org/docs/attrs/rotation/)
   */
  public var rotation: Number?

  /**
   * [https://graphviz.org/docs/attrs/scale/](https://graphviz.org/docs/attrs/scale/)
   */
  public var scale: String?

  /**
   * [https://graphviz.org/docs/attrs/searchsize/](https://graphviz.org/docs/attrs/searchsize/)
   */
  public var searchSize: Int?

  /**
   * [https://graphviz.org/docs/attrs/sep/](https://graphviz.org/docs/attrs/sep/)
   */
  public var sep: String?

  /**
   * [https://graphviz.org/docs/attrs/showboxes/](https://graphviz.org/docs/attrs/showboxes/)
   */
  public var showBoxes: Int?

  /**
   * [https://graphviz.org/docs/attrs/size/](https://graphviz.org/docs/attrs/size/)
   */
  public var size: String?

  /**
   * [https://graphviz.org/docs/attrs/smoothing/](https://graphviz.org/docs/attrs/smoothing/)
   */
  public var smoothing: SmoothType?

  /**
   * [https://graphviz.org/docs/attrs/sortv/](https://graphviz.org/docs/attrs/sortv/)
   */
  public var sortv: Int?

  /**
   * [https://graphviz.org/docs/attrs/splines/](https://graphviz.org/docs/attrs/splines/)
   */
  public var splines: String?

  /**
   * [https://graphviz.org/docs/attrs/start/](https://graphviz.org/docs/attrs/start/)
   */
  public var start: String?

  /**
   * [https://graphviz.org/docs/attrs/style/](https://graphviz.org/docs/attrs/style/)
   */
  public var style: String?

  /**
   * [https://graphviz.org/docs/attrs/stylesheet/](https://graphviz.org/docs/attrs/stylesheet/)
   */
  public var styleSheet: String?

  /**
   * [https://graphviz.org/docs/attrs/target/](https://graphviz.org/docs/attrs/target/)
   */
  public var target: String?

  /**
   * [https://graphviz.org/docs/attrs/TBbalance/](https://graphviz.org/docs/attrs/TBbalance/)
   */
  public var tbBalance: String?

  /**
   * [https://graphviz.org/docs/attrs/tooltip/](https://graphviz.org/docs/attrs/tooltip/)
   */
  public var tooltip: String?

  /**
   * [https://graphviz.org/docs/attrs/truecolor/](https://graphviz.org/docs/attrs/truecolor/)
   */
  public var trueColor: Boolean?

  /**
   * [https://graphviz.org/docs/attrs/URL/](https://graphviz.org/docs/attrs/URL/)
   */
  public var url: String?

  /**
   * [https://graphviz.org/docs/attrs/viewport/](https://graphviz.org/docs/attrs/viewport/)
   */
  public var viewPort: String?

  /**
   * [https://graphviz.org/docs/attrs/voro_margin/](https://graphviz.org/docs/attrs/voro_margin/)
   */
  public var voroMargin: Number?

  /**
   * [https://graphviz.org/docs/attrs/xdotversion/](https://graphviz.org/docs/attrs/xdotversion/)
   */
  public var xdotVersion: String?
}
