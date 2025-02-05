import scalafx.embed.swing.SwingFXUtils
import scalafx.scene.control.Button
import scalafx.scene.image.WritableImage
import scalafx.scene.layout.StackPane
import scalafx.stage.FileChooser.ExtensionFilter
import scalafx.stage.{FileChooser, Stage}
import scalafx.embed.swing.SwingFXUtils
import javax.imageio.ImageIO

class buttonMaker:
/** Method that creates a new button for saving a stackpane as a picture
  */
  def saveButtonMaker(stage: Stage, stack: StackPane): Button = new Button("Save"):
    onAction = (event) =>
      /** A new filechooser with the desired filetypes is created.*/
      val fileChooser = new FileChooser():
        extensionFilters.add(ExtensionFilter("PNG", "*.png"))
      fileChooser.setTitle("Save file")
      val fileToSave = fileChooser.showSaveDialog(stage)
      if fileToSave != null then
        /** The writable image allows for the information from the stack to be stored */
        var writable =
          new WritableImage(stack.getWidth.toInt, stack.getHeight.toInt)
        stack.snapshot(null, writable)
        var rendered = SwingFXUtils.fromFXImage(writable, null)
        ImageIO.write(rendered, "png", fileToSave)
    
  end saveButtonMaker

