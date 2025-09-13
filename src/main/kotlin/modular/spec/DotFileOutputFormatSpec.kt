/**
 * Copyright © 2025 Jon Poulton
 * SPDX-License-Identifier: Apache-2.0
 */
@file:Suppress("TooManyFunctions", "unused", "SpellCheckingInspection") // public API

package modular.spec

import modular.gradle.ModularDsl
import org.gradle.api.provider.SetProperty

/**
 * https://graphviz.org/docs/outputs/
 *
 * Which ones are supported on your machine will depend on the version of GraphViz you use.
 */
interface DotFileOutputFormatSpec {
  val outputFormats: SetProperty<String>
  @ModularDsl fun add(value: String)

  @ModularDsl fun canon() = add("canon")
  @ModularDsl fun cmap() = add("cmap")
  @ModularDsl fun cmapx() = add("cmapx")
  @ModularDsl fun cmapxNp() = add("cmapx_np")
  @ModularDsl fun dot() = add("dot")
  @ModularDsl fun dotJson() = add("dot_json")
  @ModularDsl fun eps() = add("eps")
  @ModularDsl fun fig() = add("fig")
  @ModularDsl fun gd() = add("gd")
  @ModularDsl fun gd2() = add("gd2")
  @ModularDsl fun gif() = add("gif")
  @ModularDsl fun gv() = add("gv")
  @ModularDsl fun imap() = add("imap")
  @ModularDsl fun imapNp() = add("imap_np")
  @ModularDsl fun ismap() = add("ismap")
  @ModularDsl fun jpe() = add("jpe")
  @ModularDsl fun jpeg() = add("jpeg")
  @ModularDsl fun jpg() = add("jpg")
  @ModularDsl fun json() = add("json")
  @ModularDsl fun json0() = add("json0")
  @ModularDsl fun mp() = add("mp")
  @ModularDsl fun pdf() = add("pdf")
  @ModularDsl fun pic() = add("pic")
  @ModularDsl fun plain() = add("plain")
  @ModularDsl fun plainExt() = add("plain-ext")
  @ModularDsl fun png() = add("png")
  @ModularDsl fun pov() = add("pov")
  @ModularDsl fun ps() = add("ps")
  @ModularDsl fun ps2() = add("ps2")
  @ModularDsl fun svg() = add("svg")
  @ModularDsl fun svgz() = add("svgz")
  @ModularDsl fun tk() = add("tk")
  @ModularDsl fun vdx() = add("vdx")
  @ModularDsl fun vml() = add("vml")
  @ModularDsl fun vmlz() = add("vmlz")
  @ModularDsl fun vrml() = add("vrml")
  @ModularDsl fun wbmp() = add("wbmp")
  @ModularDsl fun webp() = add("webp")
  @ModularDsl fun x11() = add("x11")
  @ModularDsl fun xdot() = add("xdot")
  @ModularDsl fun xdot12() = add("xdot1.2")
  @ModularDsl fun xdot14() = add("xdot1.4")
  @ModularDsl fun xdotJson() = add("xdot_json")
  @ModularDsl fun xlib() = add("xlib")
}
