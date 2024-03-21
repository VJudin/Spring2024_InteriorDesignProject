import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{Background, ColumnConstraints, GridPane, HBox, Pane, RowConstraints, StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Rectangle, Shape}
import scalafx.scene.paint.Color.*
import scalafx.stage.{FileChooser, Popup}

import java.awt.Desktop
import java.io.FileInputStream

/** Luokan alku, johon voi jo lisätä oman pohjapiirrustuksen, ei käsittele virheellisiä syötteitä **/

object DesingGUI extends JFXApp3:

  def start() =

    stage = new JFXApp3.PrimaryStage:
      title = "Interior Design"
      width = 1000
      height = 600

    val root = VBox()

    val scene1 = Scene(parent = root)
    stage.scene = scene1

    val sidePanel = new VBox()
    

    val rectangle2 = new Rectangle:
      x = 275
      y = 175
      width = stage.width.toDouble / 4
      height = (stage.height.toDouble-70) / 3
      fill = Red

    val rectangle3 = new Rectangle:
      x = 275
      y = 175
      width = stage.width.toDouble / 4
      height = (stage.height.toDouble-70) / 3
      fill = Green

    val root2 = GridPane()

    val testFurniture = new Furniture("Sofa", 60, 60, false, Rectangle(60, 60), 0, 0, Red, false)
    val sofaPanel = new FurniturePanel( testFurniture, (stage.width.toDouble / 4), ((stage.height.toDouble-70) / 3), root2)
    

    var mainScene = new ImageView(Image(FileInputStream("/Users/vilmajudin/Desktop/Koulu hommat/Vuosi 1/Periodi 3/MagicOfInteriorDesign/src/test/piirrustus.jpeg"))):
      fitHeight = stage.height.toDouble - 70
      fitWidth = stage.width.toDouble * 3/4


    //root2.setPrefHeight(stage.height.toDouble - 70)
    //root2.setPrefWidth(stage.width.toDouble * 3/4)

    //val canvas = Canvas( stage.width.toDouble * 3 / 4, stage.height.toDouble - 70)
    //var mainScene2 = canvas.graphicsContext2D



    var hmm = new StackPane():
      children = Array( mainScene, root2 )


    /**Luo uuden ikkunan, jonne lisätään kuvatiedoston polku**/


    val d = new HBox():
      children = Array(hmm, sidePanel)



/**    Saving code of popUp for possible later use
 *
 * val popUp = new VBox():
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
      children = Array( label, text1, button ) */


    val fileChooser = new FileChooser():
      title = "Open image of floorplan"

    //val scene2 = Scene(parent = popUp)

    val addButton = new Button( "Add floorplan"):
      onAction = (event) =>
        val floorplan = fileChooser.showOpenDialog(stage)
        if floorplan != null then
          mainScene = new ImageView(Image(FileInputStream(floorplan.getAbsolutePath))):
            fitHeight = stage.height.toDouble - 70
            fitWidth = stage.width.toDouble * 3/4
          d.children.clear()
          d.children = Array( mainScene, sidePanel)

    val bottomBar = new HBox():
      padding = Insets.apply(10, 10, 10, 10)
      spacing = 10

      background = Background.fill(White)
      children = Array(addButton)

    sidePanel.children = Array(rectangle2, rectangle3, sofaPanel)
    root.children = Array(d, bottomBar)


  end start

end DesingGUI

