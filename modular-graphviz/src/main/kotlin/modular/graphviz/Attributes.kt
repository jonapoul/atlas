/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("SpellCheckingInspection")

package modular.graphviz

import modular.core.ModularDsl
import modular.core.PropertiesSpec

/**
 * See https://graphviz.org/docs/nodes/
 */
@ModularDsl
interface NodeAttributes : PropertiesSpec {
  // TODO: https://github.com/jonapoul/modular/issues/258
  // var color: String?
  var colorScheme: String?
  var comment: String?
  var distortion: String?
  var fillColor: String?
  var fixedSize: String?
  var fontColor: String?
  var fontName: String?
  var fontSize: String?
  var gradientAngle: Int?
  var group: String?
  var height: Number?
  var href: String?
  var id: String?
  var image: String?
  var imagePos: ImagePos?
  var imageScale: String?
  var label: String?
  var labelLoc: String?
  var layer: String?
  var margin: String?
  var noJustify: Boolean?
  var ordering: String?
  var orientation: Number?
  var penWidth: Number?
  var peripheries: Int?
  var pin: Boolean?
  var pos: String?
  var rects: String?
  var regular: Boolean?
  var root: String?
  var samplePoints: Int?
  var shape: Shape?
  var shapeFile: String?
  var showBoxes: Int?
  var sides: Int?
  var skew: Number?
  var sortv: Int?
  var style: NodeStyle?
  var target: String?
  var tooltip: String?
  var url: String?
  var vertices: String?
  var width: Number?
  var xlabel: String?
  var xlp: String?
  var z: Number?
}

/**
 * See https://graphviz.org/docs/edges/
 */
@ModularDsl
interface EdgeAttributes : PropertiesSpec {
  var arrowHead: ArrowType?
  var arrowSize: Number?
  var arrowTail: ArrowType?
  var color: String?
  var colorScheme: String?
  var comment: String?
  var constraint: Boolean?
  var decorate: Boolean?
  var dir: Dir?
  var edgeHref: String?
  var edgeTarget: String?
  var edgeTooltip: String?
  var edgeUrl: String?
  var fillColor: String?
  var fontColor: String?
  var fontName: String?
  var fontSize: String?
  var headLp: String?
  var headClip: Boolean?
  var headHref: String?
  var headLabel: String?
  var headPort: String?
  var headTarget: String?
  var headTooltip: String?
  var headUrl: String?
  var href: String?
  var id: String?
  var label: String?
  var labelAngle: Number?
  var labelDistance: Number?
  var labelFloat: Boolean?
  var labelFontColor: String?
  var labelFontName: String?
  var labelFontSize: String?
  var labelHref: String?
  var labelTarget: String?
  var labelTooltip: String?
  var labelUrl: String?
  var layer: String?
  var len: Number?
  var lhead: String?
  var lp: String?
  var ltail: String?
  var minLen: Int?
  var noJustify: Boolean?
  var penWidth: Number?
  var pos: String?
  var sameHead: String?
  var sameTail: String?
  var showBoxes: Int?
  var style: LinkStyle?
  var tailLp: String?
  var tailClip: Boolean?
  var tailHref: String?
  var tailLabel: String?
  var tailPort: String?
  var tailTarget: String?
  var tailTooltip: String?
  var tailUrl: String?
  var target: String?
  var tooltip: String?
  var url: String?
  var weight: Number?
  var xLabel: String?
  var xlp: String?
}

@ModularDsl
interface GraphAttributes : PropertiesSpec {
  var background: String?
  var bb: String?
  var beautify: Boolean?
  var bgColor: String?
  var center: Boolean?
  var charset: String?
  var clusterRank: ClusterMode?
  var colorScheme: String?
  var comment: String?
  var compound: Boolean?
  var concentrate: Boolean?
  var damping: Float?
  var defaultDist: Number?
  var dim: Int?
  var dimen: Int?
  var dirEdgeConstraints: String?
  var dpi: Number?
  var epsilon: Float?
  var esep: String?
  var fontColor: String?
  var fontName: String?
  var fontNames: String?
  var fontPath: String?
  var fontSize: String?
  var forceLabels: Boolean?
  var gradientAngle: Int?
  var href: String?
  var id: String?
  var imagePath: String?
  var inputScale: Number?
  var k: Number?
  var label: String?
  var labelScheme: Int?
  var labelJust: String?
  var labelLoc: String?
  var landscape: Boolean?
  var layerListSep: String?
  var layers: String?
  var layerSelect: String?
  var layerSep: String?
  var layout: LayoutEngine?
  var levels: Int?
  var levelsGap: Number?
  var lheight: Number?
  var lineLength: Int?
  var lp: String?
  var lWidth: Number?
  var margin: String?
  var maxiter: Int?
  var mcLimit: Number?
  var minDist: Number?
  var mode: String?
  var model: String?
  var newRank: Boolean?
  var nodeSep: Number?
  var noJustify: Boolean?
  var normalize: String?
  var noTranslate: Boolean?
  var nsLimit: Number?
  var nsLimit1: Number?
  var oneBlock: Boolean?
  var ordering: String?
  var orientation: Number?
  var outputOrder: String?
  var overlap: String?
  var overlapScaling: Number?
  var overlapShrink: Boolean?
  var pack: String?
  var packMode: String?
  var pad: String?
  var page: String?
  var pageDir: String?
  var quadTree: String?
  var quantum: Number?
  var rankDir: RankDir?
  var rankSep: Number?
  var ratio: String?
  var reminCross: Boolean?
  var repulsiveForce: Number?
  var resolution: Number?
  var root: String?
  var rotate: Int?
  var rotation: Number?
  var scale: String?
  var searchSize: Int?
  var sep: String?
  var showBoxes: Int?
  var size: String?
  var smoothing: SmoothType?
  var sortv: Int?
  var splines: String?
  var start: String?
  var style: String?
  var styleSheet: String?
  var target: String?
  var tbBalance: String?
  var tooltip: String?
  var trueColor: Boolean?
  var url: String?
  var viewPort: String?
  var voroMargin: Number?
  var xdotVersion: String?
}
