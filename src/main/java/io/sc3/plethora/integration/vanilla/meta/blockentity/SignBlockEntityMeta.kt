package io.sc3.plethora.integration.vanilla.meta.blockentity

import io.sc3.plethora.api.meta.BasicMetaProvider
import net.minecraft.block.entity.SignBlockEntity

class SignBlockEntityMeta : BasicMetaProvider<SignBlockEntity>(
  "Provides the text upon the sign."
) {
  override fun getMeta(sign: SignBlockEntity): Map<String, *> {
    return mapOf("lines" to sign.lines)
  }
}

val SignBlockEntity.lines: Map<Int, String>
  get() = (1 .. 4).associateWith { getTextOnRow(it - 1, true).string }
