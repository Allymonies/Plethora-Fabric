package io.sc3.plethora.gameplay.client.gui

import com.mojang.blaze3d.systems.RenderSystem
import dan200.computercraft.client.gui.AbstractComputerScreen
import dan200.computercraft.client.gui.widgets.ComputerSidebar
import dan200.computercraft.client.gui.widgets.DynamicImageButton
import dan200.computercraft.client.gui.widgets.TerminalWidget
import dan200.computercraft.client.render.ComputerBorderRenderer
import dan200.computercraft.shared.computer.inventory.AbstractComputerMenu
import net.minecraft.client.gui.tooltip.Tooltip
import net.minecraft.client.render.GameRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.slot.Slot
import net.minecraft.text.Text
import net.minecraft.text.Text.translatable
import io.sc3.plethora.Plethora.ModId
import io.sc3.plethora.gameplay.neural.NeuralInterfaceScreenHandler
import io.sc3.plethora.gameplay.neural.NeuralInterfaceScreenHandler.Companion.NEURAL_START_X
import io.sc3.plethora.gameplay.neural.NeuralInterfaceScreenHandler.Companion.S
import io.sc3.plethora.gameplay.neural.NeuralInterfaceScreenHandler.Companion.START_Y
import io.sc3.plethora.gameplay.neural.NeuralInterfaceScreenHandler.Companion.slotPositions
import io.sc3.plethora.gameplay.neural.NeuralInterfaceScreenHandler.Companion.swapBtn

class NeuralInterfaceScreen(
  private val container: NeuralInterfaceScreenHandler,
  playerInv: PlayerInventory,
  title: Text,
) : AbstractComputerScreen<NeuralInterfaceScreenHandler>(
  container, playerInv, translatable("gui.plethora.neuralInterface.title"), BORDER
) {
  private var peripherals = true

  override fun init() {
    backgroundWidth = TEX_WIDTH + AbstractComputerMenu.SIDEBAR_WIDTH
    backgroundHeight = TEX_HEIGHT
    super.init()
  }

  fun initNeural() {
    // Draw the button to swap between peripherals/modules view
    addDrawableChild(PlethoraDynamicImageButton(
      x + swapBtn.x(), y + swapBtn.y(), 16, 16,
      { if (peripherals) 0 else 16 },  // Show the appropriate icon based on the current view
      ICON_Y, 0, tex, TEX_SIZE, TEX_SIZE,
      { // Swap view on click
        peripherals = !peripherals
        updateVisible()
      }
    ) {
      // Show the correct tooltip for the view
      if (peripherals) TOOLTIP_MODULES else TOOLTIP_PERIPHERALS
    })

    updateVisible() // Make one set of peripheral/module slots visible
  }

  override fun createTerminal() = TerminalWidget(
    terminalData, input,
    x + BORDER + AbstractComputerMenu.SIDEBAR_WIDTH,
    y + BORDER
  )

  override fun drawBackground(matrices: MatrixStack, delta: Float, mouseX: Int, mouseY: Int) {
    RenderSystem.setShader(GameRenderer::getPositionTexProgram)
    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
    RenderSystem.setShaderTexture(0, tex)

    drawTexture(matrices, x + AbstractComputerMenu.SIDEBAR_WIDTH, y, 0, 0, TEX_WIDTH, TEX_HEIGHT)

    // Peripheral direction overlay
    if (peripherals) {
      drawTexture(matrices, x + NEURAL_START_X + 1 + S, y + START_Y + 1, 32, ICON_Y, 16, 16); // Top
      drawTexture(matrices, x + NEURAL_START_X + 1, y + START_Y + 1 + S, 50, ICON_Y, 16 * 3, 16); // Middle 3
      drawTexture(matrices, x + NEURAL_START_X + 1 + S, y + START_Y + 1 + 2 * S, 104, ICON_Y, 16, 16); // Bottom
    }

    RenderSystem.setShaderTexture(0, ComputerBorderRenderer.BACKGROUND_NORMAL)
    ComputerSidebar.renderBackground(matrices, x, y + sidebarYOffset)
  }

  private fun updateVisible() {
    setVisible(container.peripheralSlots, peripherals)
    setVisible(container.moduleSlots, !peripherals)
  }

  companion object {
    private val tex = ModId("textures/gui/neural_interface.png")
    private const val ICON_Y = 224

    private const val TEX_SIZE = 256
    private const val TEX_WIDTH = 254
    private const val TEX_HEIGHT = 217

    private val modulesText = translatable("gui.plethora.neuralInterface.modules")
    private val TOOLTIP_MODULES = DynamicImageButton.HintedMessage(modulesText, Tooltip.of(modulesText))

    private val peripheralsText = translatable("gui.plethora.neuralInterface.peripherals")
    private val TOOLTIP_PERIPHERALS = DynamicImageButton.HintedMessage(peripheralsText, Tooltip.of(peripheralsText))

    const val BORDER = 8

    private fun setVisible(slots: List<Slot>, visible: Boolean) {
      slots.forEachIndexed { i, slot ->
        if (visible) {
          val pos = slotPositions[i]
          slot.x = pos.x()
          slot.y = pos.y()
        } else {
          slot.x = -20000
          slot.y = -20000
        }
      }
    }
  }
}
