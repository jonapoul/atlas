/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("TooManyFunctions", "unused", "SpellCheckingInspection") // public API

package modular.graphviz.spec

import modular.gradle.ModularDsl

/**
 * The formats supported on your machine will depend on the version of GraphViz you use.
 *
 * If you specify multiple formats, you'll need to also pass `includeInReadme=true` to one of them. Without this,
 * [modular.core.tasks.WriteReadme] won't know which one of the generated files to include.
 *
 * See https://graphviz.org/docs/outputs/
 */
interface GraphVizFileFormatSpec {
  @ModularDsl fun add(element: String, includeInReadme: Boolean)

  @ModularDsl fun canon(includeInReadme: Boolean = false) = add("canon", includeInReadme)
  @ModularDsl fun cmap(includeInReadme: Boolean = false) = add("cmap", includeInReadme)
  @ModularDsl fun cmapx(includeInReadme: Boolean = false) = add("cmapx", includeInReadme)
  @ModularDsl fun cmapxNp(includeInReadme: Boolean = false) = add("cmapx_np", includeInReadme)
  @ModularDsl fun dot(includeInReadme: Boolean = false) = add("dot", includeInReadme)
  @ModularDsl fun dotJson(includeInReadme: Boolean = false) = add("dot_json", includeInReadme)
  @ModularDsl fun eps(includeInReadme: Boolean = false) = add("eps", includeInReadme)
  @ModularDsl fun fig(includeInReadme: Boolean = false) = add("fig", includeInReadme)
  @ModularDsl fun gd(includeInReadme: Boolean = false) = add("gd", includeInReadme)
  @ModularDsl fun gd2(includeInReadme: Boolean = false) = add("gd2", includeInReadme)
  @ModularDsl fun gif(includeInReadme: Boolean = false) = add("gif", includeInReadme)
  @ModularDsl fun gv(includeInReadme: Boolean = false) = add("gv", includeInReadme)
  @ModularDsl fun imap(includeInReadme: Boolean = false) = add("imap", includeInReadme)
  @ModularDsl fun imapNp(includeInReadme: Boolean = false) = add("imap_np", includeInReadme)
  @ModularDsl fun ismap(includeInReadme: Boolean = false) = add("ismap", includeInReadme)
  @ModularDsl fun jpe(includeInReadme: Boolean = false) = add("jpe", includeInReadme)
  @ModularDsl fun jpeg(includeInReadme: Boolean = false) = add("jpeg", includeInReadme)
  @ModularDsl fun jpg(includeInReadme: Boolean = false) = add("jpg", includeInReadme)
  @ModularDsl fun json(includeInReadme: Boolean = false) = add("json", includeInReadme)
  @ModularDsl fun json0(includeInReadme: Boolean = false) = add("json0", includeInReadme)
  @ModularDsl fun mp(includeInReadme: Boolean = false) = add("mp", includeInReadme)
  @ModularDsl fun pdf(includeInReadme: Boolean = false) = add("pdf", includeInReadme)
  @ModularDsl fun pic(includeInReadme: Boolean = false) = add("pic", includeInReadme)
  @ModularDsl fun plain(includeInReadme: Boolean = false) = add("plain", includeInReadme)
  @ModularDsl fun plainExt(includeInReadme: Boolean = false) = add("plain-ext", includeInReadme)
  @ModularDsl fun png(includeInReadme: Boolean = false) = add("png", includeInReadme)
  @ModularDsl fun pov(includeInReadme: Boolean = false) = add("pov", includeInReadme)
  @ModularDsl fun ps(includeInReadme: Boolean = false) = add("ps", includeInReadme)
  @ModularDsl fun ps2(includeInReadme: Boolean = false) = add("ps2", includeInReadme)
  @ModularDsl fun svg(includeInReadme: Boolean = false) = add("svg", includeInReadme)
  @ModularDsl fun svgz(includeInReadme: Boolean = false) = add("svgz", includeInReadme)
  @ModularDsl fun tk(includeInReadme: Boolean = false) = add("tk", includeInReadme)
  @ModularDsl fun vdx(includeInReadme: Boolean = false) = add("vdx", includeInReadme)
  @ModularDsl fun vml(includeInReadme: Boolean = false) = add("vml", includeInReadme)
  @ModularDsl fun vmlz(includeInReadme: Boolean = false) = add("vmlz", includeInReadme)
  @ModularDsl fun vrml(includeInReadme: Boolean = false) = add("vrml", includeInReadme)
  @ModularDsl fun wbmp(includeInReadme: Boolean = false) = add("wbmp", includeInReadme)
  @ModularDsl fun webp(includeInReadme: Boolean = false) = add("webp", includeInReadme)
  @ModularDsl fun x11(includeInReadme: Boolean = false) = add("x11", includeInReadme)
  @ModularDsl fun xdot(includeInReadme: Boolean = false) = add("xdot", includeInReadme)
  @ModularDsl fun xdot12(includeInReadme: Boolean = false) = add("xdot1.2", includeInReadme)
  @ModularDsl fun xdot14(includeInReadme: Boolean = false) = add("xdot1.4", includeInReadme)
  @ModularDsl fun xdotJson(includeInReadme: Boolean = false) = add("xdot_json", includeInReadme)
  @ModularDsl fun xlib(includeInReadme: Boolean = false) = add("xlib", includeInReadme)
}
