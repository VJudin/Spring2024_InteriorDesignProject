import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{Background, ColumnConstraints, GridPane, HBox, Pane, RowConstraints, VBox}
import scalafx.scene.shape.Rectangle
import scalafx.scene.paint.Color.*
import scalafx.stage.Popup

import java.io.FileInputStream

/** Luokan tynkä, jota on tarkoitus muokata. **/

object DesingGUI extends JFXApp3:

  def start() =

    stage = new JFXApp3.PrimaryStage:
      title = "Interior Design"
      width = 600
      height = 450

    val root = VBox()

    val scene1 = Scene(parent = root)
    stage.scene = scene1

    val sidePanel = new VBox()

    val rectangle2 = new Rectangle:
      x = 275
      y = 175
      width = stage.width.toDouble / 4
      height = (stage.height.toDouble-70) / 2
      fill = Red

    val rectangle3 = new Rectangle:
      x = 275
      y = 175
      width = stage.width.toDouble / 4
      height = (stage.height.toDouble-70) / 2
      fill = Green

    var mainScene = new ImageView(Image(FileInputStream("/Users/vilmajudin/Desktop/Koulu hommat/Vuosi 1/Periodi 3/MagicOfInteriorDesign/src/test/piirrustus.jpeg"))):
      fitHeight = stage.height.toDouble - 70
      fitWidth = stage.width.toDouble * 3/4

    /**Luo uuden ikkunan, jonne lisätään kuvatiedoston polku**/


    val d = new HBox():
      children = Array(mainScene, sidePanel)


    val popUp = new VBox():
      val label = Label("Add the path to your floorplan")
      val text1 = TextField()
      val button = new Button("Submit"):
        onAction = (event) =>
          mainScene = new ImageView(Image(FileInputStream(text1.text.value))):
                  fitHeight = stage.height.toDouble - 70
                  fitWidth = stage.width.toDouble * 3/4
          stage.scene = scene1
          d.children.clear()
          d.children = Array( mainScene, sidePanel)
          text1.clear()
      text1.promptText = "Add the path to your floorplan here"
      children = Array( label, text1, button )

    val scene2 = Scene(parent = popUp)

    val addButton = new Button( "Add floorplan"):
      onAction = (event) => stage.scene = scene2

    val bottomBar = new HBox():
      padding = Insets.apply(10, 10, 10, 10)
      spacing = 10

      background = Background.fill(White)
      children = Array(addButton)

    sidePanel.children = Array(rectangle2, rectangle3)
    root.children = Array(d, bottomBar)


  end start

end DesingGUI

