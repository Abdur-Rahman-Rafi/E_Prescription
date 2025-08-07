package sample;

import com.pdfjet.*;
import com.pdfjet.Cell;
import com.pdfjet.Page;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.lang.String;
import java.time.LocalDate;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;




public class P_Dashboard implements Initializable {

   /* private Client client;

    public void initialize() {
        client = new Client();
    }*/






    public static String pkey=LoginController.pkey;



    @FXML
    private Button p_btnhome;

    @FXML
    private Button p_btnpres;

    @FXML
    private Button p_btnedit;

    @FXML
    private Circle btnminimize;

    @FXML
    private Circle btnfullScreen;
    @FXML
    private Button p_btnsignout;

    @FXML
    private Pane pnPatient;

    @FXML
    private Circle btnclose2;

    @FXML
    private TextField p_tfname;

    @FXML
    private TextField p_tfphone;

    @FXML
    private TextField p_tfemail;

    @FXML
    private Button p_btnsave;

    @FXML
    private Button btncancel;

    @FXML
    private TextField p_tfage;

    @FXML
    private DatePicker p_tfdob;

    @FXML
    private ComboBox<String> genCombo3;

    @FXML
    private Pane pnHome;

    @FXML
    private Label P_welcome;

    @FXML
    private Label p_name;

    @FXML
    private Label p_gender;

    @FXML
    private Label p_phone;

    @FXML
    private Label p_age;

    @FXML
    private Circle btnclose1;

    @FXML
    private Label p_gender1;

    @FXML
    private Label p_gender11;

    @FXML
    private Label p_gender111;

    @FXML
    private Label p_gender1111;

    @FXML
    private Label p_home_age;

    @FXML
    private Pane pnPrescription;

    @FXML
    private Circle btnclose;

    @FXML
    private Button p_btnchat;

    @FXML
    private TableView<Patient_info> pdf_table;

    @FXML
    private TableColumn<Patient_info, String> pdf_id;

    @FXML
    private TableColumn<Patient_info, String> pdf_name;

    @FXML
    private TableColumn<Patient_info, String> pdf_dose;

    @FXML
    private TableColumn<Patient_info, String> pdf_strength;

    @FXML
    private TableColumn<Patient_info, String> pdf_advice;

    @FXML
    private TableColumn<Patient_info, String> pdf_duration;

    @FXML
    private Button pdfBtn;


    ObservableList<String> list = FXCollections.observableArrayList("Male","Female", "Other");
    ObservableList<Patient_info> list2= FXCollections.observableArrayList();

    @FXML
    void handleButtonAction(ActionEvent event) throws Exception {


if(event.getSource().equals(p_btnchat)){
            // Start the chat
            try {
               // client.start();
               Stage chatStage = new Stage();
            patientChat chat = new patientChat();
            chat.start(chatStage);
            } catch (Exception e) {
                // Handle any exceptions
                e.printStackTrace();
            }

        }

        if(event.getSource().equals(p_btnhome))
        {
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/preskibo", "root", "");
                String sql = "select * from patient where ID='"+pkey+"';";

                PreparedStatement ps= con.prepareStatement(sql);
                //ps.setString(1,pn_combo.getValue());
                ResultSet rs = ps.executeQuery();


                if(rs.next()) {
                    p_name.setText(new String( rs.getString(2)));
                    p_phone.setText(new String( rs.getString(4)));
                    p_home_age.setText(new String( rs.getString(3)));
                    p_gender.setText(new String( rs.getString(7)));


                }
            }catch (Exception e){
                e.printStackTrace();
            }


            pnHome.toFront();
        }
        if(event.getSource().equals(p_btnpres)) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection co = DriverManager.getConnection("jdbc:mysql://localhost:3306/preskibo", "root", "");
                PreparedStatement ps;
                ResultSet rs;

                String query = "SELECT* From prescription where Pt_ID=?";
                ps = co.prepareStatement(query);

                ps.setString(1, pkey);
                rs = ps.executeQuery();

                while (rs.next()) {
                    String prescriptionId = rs.getString("Pt_ID");
                    boolean prescriptionExists = false;
                    for (Patient_info info : list2) {
                        if (prescriptionId.equals(info.getId())) {
                            prescriptionExists = true;
                            break;
                        }
                    }
                    if (!prescriptionExists) {
                        list2.add(new Patient_info(
                                rs.getString("Pt_ID"),
                                rs.getString("Medicine_Name"),
                                rs.getString("Dose"),
                                rs.getString("Advice"),
                                rs.getString("Duration"),
                                rs.getString("Strength")));
                    }
                    pdf_table.setItems(list2);
                }

                pdf_id.setCellValueFactory(new PropertyValueFactory<>("id"));
                pdf_name.setCellValueFactory(new PropertyValueFactory<>("med_name"));
                pdf_dose.setCellValueFactory(new PropertyValueFactory<>("dose"));
                pdf_advice.setCellValueFactory(new PropertyValueFactory<>("advice"));
                pdf_duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
                pdf_strength.setCellValueFactory(new PropertyValueFactory<>("strength"));

            } catch (Exception e) {
                e.printStackTrace();
            }
            pnPrescription.toFront();
        }

        if(event.getSource().equals(p_btnsignout))
        {
            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            Scene primaryStage = new Scene(root);
            Main.mainStage.setScene(primaryStage);
        }
        if(event.getSource().equals(btncancel))
        {
            pnHome.toFront();
        }
        if(event.getSource().equals(p_btnedit))
        {
            pnPatient.toFront();
        }

        if(event.getSource().equals(p_btnsave))
        {

            Connection con= null;
            PreparedStatement ps = null;

            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                con= DriverManager.getConnection("jdbc:mysql://localhost:3306/preskibo", "root", "");
                String sql = "UPDATE patient SET Name=?,Email=?,Phone=?,Age=?,Gender=?, DOB=? where ID =?";

                ps= con.prepareStatement(sql);
                ps.setString(1,p_tfname.getText());
                ps.setString(2,p_tfemail.getText());
                ps.setString(3,p_tfphone.getText());
                ps.setString(4,p_tfage.getText());
                ps.setString(5,genCombo3.getValue());
                ps.setString(6,((TextField)p_tfdob.getEditor()).getText());
                ps.setString(7,pkey);


                int i = ps.executeUpdate();

                if(i>0){
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle("Edit Info");
                    a.setHeaderText(" Information Updated Successfully");
                    a.setContentText("Your new information are now stored in database");
                    a.showAndWait();
                }
            }
            catch (Exception e){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText("Error in Login Account");
                a.setContentText("Your Account is not created Because of Technical Issue\n"+e.getMessage());
                a.showAndWait();
            }
        }
    }

    @FXML
    void handleMouseEvent(MouseEvent event) throws FileNotFoundException {
        if(event.getSource()==btnclose||event.getSource()==btnclose1||event.getSource()==btnclose2)
        {
            System.exit(0);
        }
        if (event.getSource() == btnminimize) {
            // Minimize the window
            ((Stage)((Circle)event.getSource()).getScene().getWindow()).setIconified(true);
        }

        if (event.getSource() == btnfullScreen) {
            // Toggle full screen
            Stage stage = (Stage)((Circle)event.getSource()).getScene().getWindow();
            stage.setFullScreen(!stage.isFullScreen());
        }

        if(event.getSource().equals(pdfBtn)){

            File f = new File("E:/UIU/6th Trimester/Aoop/Project/e-prescription/sample.pdf");
            FileOutputStream fos = new FileOutputStream(f);
            try {
                PDF pdf = new PDF(fos);

                // Add a new page to the PDF
                Page page = new Page(pdf, A4.PORTRAIT);
                Font titleFont = new Font(pdf, CoreFont.HELVETICA_BOLD);
                Font headerFont = new Font(pdf, CoreFont.HELVETICA_BOLD);
                Font bodyFont = new Font(pdf, CoreFont.HELVETICA);

                // Title Section
                TextLine title = new TextLine(titleFont, "PresCripto");
                title.setPosition(250, 50);  // Center the title
                title.drawOn(page);

                // Date and other info
                TextLine dateLine = new TextLine(bodyFont, "Date: " + LocalDate.now().toString());
                dateLine.setPosition(50, 80);  // Positioning below the title
                dateLine.drawOn(page);

                // Table Headers
                Table table = new Table();
                List<List<Cell>> tableData = new ArrayList<>();

                List<Cell> headerRow = new ArrayList<>();
                /*headerRow.add(new Cell(headerFont, "Patient ID"));
                headerRow.add(new Cell(headerFont, "Medicine Name"));
                headerRow.add(new Cell(headerFont, "Dose"));
                headerRow.add(new Cell(headerFont, "Advice"));
                headerRow.add(new Cell(headerFont, "Duration"));
                headerRow.add(new Cell(headerFont, "Strength"));
                tableData.add(headerRow);*/

                // Adding data from the UI
                List<Cell> tablerow = new ArrayList<>();
                tablerow.add(new Cell(bodyFont, pdf_id.getText()));
                tablerow.add(new Cell(bodyFont, pdf_name.getText()));
                tablerow.add(new Cell(bodyFont, pdf_dose.getText()));
                tablerow.add(new Cell(bodyFont, pdf_advice.getText()));
                tablerow.add(new Cell(bodyFont, pdf_duration.getText()));
                tablerow.add(new Cell(bodyFont, pdf_strength.getText()));
                tableData.add(tablerow);

                List<Patient_info> items = pdf_table.getItems();

                // Populate the table with patient information
                for (Patient_info comm : items) {
                    List<Cell> row = new ArrayList<>();
                    row.add(new Cell(bodyFont, comm.getId()));
                    row.add(new Cell(bodyFont, comm.getMed_name()));
                    row.add(new Cell(bodyFont, comm.getDose()));
                    row.add(new Cell(bodyFont, comm.getAdvice()));
                    row.add(new Cell(bodyFont, comm.getDuration()));
                    row.add(new Cell(bodyFont, comm.getStrength()));
                    tableData.add(row);
                }

                // Set the table data and format
                table.setData(tableData);
                table.setPosition(50f, 120f);  // Adjust position to avoid title overlap

                // Column widths to ensure proper spacing
                table.setColumnWidth(0, 80f);
                table.setColumnWidth(1, 120f);
                table.setColumnWidth(2, 60f);
                table.setColumnWidth(3, 80f);
                table.setColumnWidth(4, 60f);
                table.setColumnWidth(5, 80f);

                // Drawing the table on multiple pages if necessary
                while (true) {
                    table.drawOn(page);
                    if (!table.hasMoreData()) {
                        table.resetRenderedPagesCount();
                        break;
                    }
                    page = new Page(pdf, A4.PORTRAIT);
                }

                // Optionally add a footer or contact information at the end
                TextLine footer = new TextLine(bodyFont, "Thank you for using E-Prescription");
                footer.setPosition(50, page.getHeight() - 50);  // Position at bottom
                footer.drawOn(page);

                pdf.flush();
                fos.close();
                // Display a success prompt
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("PDF Downloaded");
                alert.setHeaderText(null);
                alert.setContentText("The PDF has been successfully generated and saved.");
                alert.showAndWait();

            } catch (Exception e) {
                System.out.println("Error occurred during PDF generation: " + e.getMessage());
                e.printStackTrace();
            }



        }
    }

    public void initialize(URL url, ResourceBundle rb){
        genCombo3.setItems(list);
    }

}