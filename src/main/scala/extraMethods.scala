import DesingGUI.{floorPlanScaleX, floorPlanScaleY}
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, Spinner}
import scalafx.scene.layout.{Background, Border, BorderImage, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii, HBox, StackPane, VBox}
import scalafx.scene.paint.Color.{Black, Pink}
import scalafx.stage.Stage

import scala.collection.mutable.ListBuffer

object extraMethods {
  
  def bottomBarMaker(): HBox =
    new HBox():
      val stroke = BorderStroke(
        Black,
        BorderStrokeStyle.Solid,
        CornerRadii(1),
        BorderWidths(5)
      )
      padding = Insets.apply(17.5, 17.5, 17.5, 17.5)
      spacing = 10
      background = Background.fill(Pink)
      border = Border(Array(stroke), Array[BorderImage]())
  end bottomBarMaker
  
  /** 
   * Method that allows the user to scale the floorplan to their liking, they can change
   * the width and lenght of the floorplan which changes the scale. Sadly causes distortion
   * to the shapes
    */
  def scaleInput(l: ListBuffer[Furniture], s: StackPane, s2: Stage): Stage =

    val stage = new Stage()
    stage.setWidth(300)
    stage.setHeight(300)
    stage.setX(300)
    stage.setY(300)

    val pane = new VBox()

    val label1 = new Label("The width of your floorplan")
    val label2 = new Label("The height of your floorplan")
    val label3 = new Label("m")
    val label4 = new Label("m")

    val widthField = new Spinner[Double](1, 20, 5, 0.5)
    val heightField = new Spinner[Double](1, 20, 5, 0.5)

    val scalingWidth = new HBox():
      spacing = 5
      children = Array(widthField, label3)

    val scalingHeight = new HBox():
      spacing = 5
      children = Array(heightField, label4)

    val submit = new Button("Submit"):
      onAction = (event) =>
        /** Muutetaan skaalaa ja skaalataan kaikki olemassa olevat huonekalut
          * kyseiseen skaalaan
          */
        floorPlanScaleX = (1000 * 3 / 4) / (widthField.getValue * 100)
        floorPlanScaleY = (600 * 4 / 5) / (heightField.getValue * 100)
        stage.close()
        l.foreach(x => x.shape.setScaleX(floorPlanScaleX))
        l.foreach(x => x.shape.setScaleY(floorPlanScaleY))

    pane.children = Array(label1, scalingWidth, label2, scalingHeight, submit)

    val scene = new Scene(pane)
    stage.setScene(scene)
    stage

  end scaleInput
}
