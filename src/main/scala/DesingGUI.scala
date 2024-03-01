import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.layout.{HBox, Pane, VBox}
import scalafx.scene.shape.Rectangle
import scalafx.scene.paint.Color.*

/** Luokan tynkä, jota on tarkoitus muokata. **/

object DesingGUI extends JFXApp3:

  def start() =

    stage = new JFXApp3.PrimaryStage:
      title = "Interior Design"
      width = 600
      height = 450

    val root = HBox()

    val scene = Scene(parent = root)
    stage.scene = scene

    val sidePanel = new VBox()

    val rectangle2 = new Rectangle:
      x = 275
      y = 175
      width = stage.width.toDouble / 4
      height = stage.height.toDouble / 2
      fill = Red

    val rectangle3 = new Rectangle:
      x = 275
      y = 175
      width = stage.width.toDouble / 4
      height = stage.height.toDouble / 2
      fill = Green

    val mainScene = new Rectangle:
      x = 275
      y = 175
      width = stage.width.toDouble * 3/4
      height = stage.height.toDouble
      fill = Blue

    sidePanel.children = Array(rectangle2, rectangle3)
    root.children = Array(mainScene, sidePanel)

  end start

end DesingGUI

