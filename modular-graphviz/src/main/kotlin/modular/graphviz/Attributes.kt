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
public interface NodeAttributes : PropertiesSpec {
  public var lineColor: String?
  public var colorScheme: String?
  public var comment: String?
  public var distortion: String?
  public var fillColor: String?
  public var fixedSize: String?
  public var fontColor: String?
  public var fontName: String?
  public var fontSize: String?
  public var gradientAngle: Int?
  public var group: String?
  public var height: Number?
  public var href: String?
  public var id: String?
  public var image: String?
  public var imagePos: ImagePos?
  public var imageScale: String?
  public var label: String?
  public var labelLoc: String?
  public var layer: String?
  public var margin: String?
  public var noJustify: Boolean?
  public var ordering: String?
  public var orientation: Number?
  public var penWidth: Number?
  public var peripheries: Int?
  public var pin: Boolean?
  public var pos: String?
  public var rects: String?
  public var regular: Boolean?
  public var root: String?
  public var samplePoints: Int?
  public var shape: Shape?
  public var shapeFile: String?
  public var showBoxes: Int?
  public var sides: Int?
  public var skew: Number?
  public var sortv: Int?
  public var style: NodeStyle?
  public var target: String?
  public var tooltip: String?
  public var url: String?
  public var vertices: String?
  public var width: Number?
  public var xlabel: String?
  public var xlp: String?
  public var z: Number?
}

/**
 * See https://graphviz.org/docs/edges/
 */
@ModularDsl
public interface EdgeAttributes : PropertiesSpec {
  public var arrowHead: ArrowType?
  public var arrowSize: Number?
  public var arrowTail: ArrowType?
  public var color: String?
  public var colorScheme: String?
  public var comment: String?
  public var constraint: Boolean?
  public var decorate: Boolean?
  public var dir: Dir?
  public var edgeHref: String?
  public var edgeTarget: String?
  public var edgeTooltip: String?
  public var edgeUrl: String?
  public var fillColor: String?
  public var fontColor: String?
  public var fontName: String?
  public var fontSize: String?
  public var headLp: String?
  public var headClip: Boolean?
  public var headHref: String?
  public var headLabel: String?
  public var headPort: String?
  public var headTarget: String?
  public var headTooltip: String?
  public var headUrl: String?
  public var href: String?
  public var id: String?
  public var label: String?
  public var labelAngle: Number?
  public var labelDistance: Number?
  public var labelFloat: Boolean?
  public var labelFontColor: String?
  public var labelFontName: String?
  public var labelFontSize: String?
  public var labelHref: String?
  public var labelTarget: String?
  public var labelTooltip: String?
  public var labelUrl: String?
  public var layer: String?
  public var len: Number?
  public var lhead: String?
  public var lp: String?
  public var ltail: String?
  public var minLen: Int?
  public var noJustify: Boolean?
  public var penWidth: Number?
  public var pos: String?
  public var sameHead: String?
  public var sameTail: String?
  public var showBoxes: Int?
  public var style: LinkStyle?
  public var tailLp: String?
  public var tailClip: Boolean?
  public var tailHref: String?
  public var tailLabel: String?
  public var tailPort: String?
  public var tailTarget: String?
  public var tailTooltip: String?
  public var tailUrl: String?
  public var target: String?
  public var tooltip: String?
  public var url: String?
  public var weight: Number?
  public var xLabel: String?
  public var xlp: String?
}

@ModularDsl
public interface GraphAttributes : PropertiesSpec {
  public var background: String?
  public var bb: String?
  public var beautify: Boolean?
  public var bgColor: String?
  public var center: Boolean?
  public var charset: String?
  public var clusterRank: ClusterMode?
  public var colorScheme: String?
  public var comment: String?
  public var compound: Boolean?
  public var concentrate: Boolean?
  public var damping: Float?
  public var defaultDist: Number?
  public var dim: Int?
  public var dimen: Int?
  public var dirEdgeConstraints: String?
  public var dpi: Number?
  public var epsilon: Float?
  public var esep: String?
  public var fontColor: String?
  public var fontName: String?
  public var fontNames: String?
  public var fontPath: String?
  public var fontSize: String?
  public var forceLabels: Boolean?
  public var gradientAngle: Int?
  public var href: String?
  public var id: String?
  public var imagePath: String?
  public var inputScale: Number?
  public var k: Number?
  public var label: String?
  public var labelScheme: Int?
  public var labelJust: String?
  public var labelLoc: String?
  public var landscape: Boolean?
  public var layerListSep: String?
  public var layers: String?
  public var layerSelect: String?
  public var layerSep: String?
  public var layout: LayoutEngine?
  public var levels: Int?
  public var levelsGap: Number?
  public var lheight: Number?
  public var lineLength: Int?
  public var lp: String?
  public var lWidth: Number?
  public var margin: String?
  public var maxiter: Int?
  public var mcLimit: Number?
  public var minDist: Number?
  public var mode: String?
  public var model: String?
  public var newRank: Boolean?
  public var nodeSep: Number?
  public var noJustify: Boolean?
  public var normalize: String?
  public var noTranslate: Boolean?
  public var nsLimit: Number?
  public var nsLimit1: Number?
  public var oneBlock: Boolean?
  public var ordering: String?
  public var orientation: Number?
  public var outputOrder: String?
  public var overlap: String?
  public var overlapScaling: Number?
  public var overlapShrink: Boolean?
  public var pack: String?
  public var packMode: String?
  public var pad: String?
  public var page: String?
  public var pageDir: String?
  public var quadTree: String?
  public var quantum: Number?
  public var rankDir: RankDir?
  public var rankSep: Number?
  public var ratio: String?
  public var reminCross: Boolean?
  public var repulsiveForce: Number?
  public var resolution: Number?
  public var root: String?
  public var rotate: Int?
  public var rotation: Number?
  public var scale: String?
  public var searchSize: Int?
  public var sep: String?
  public var showBoxes: Int?
  public var size: String?
  public var smoothing: SmoothType?
  public var sortv: Int?
  public var splines: String?
  public var start: String?
  public var style: String?
  public var styleSheet: String?
  public var target: String?
  public var tbBalance: String?
  public var tooltip: String?
  public var trueColor: Boolean?
  public var url: String?
  public var viewPort: String?
  public var voroMargin: Number?
  public var xdotVersion: String?
}
