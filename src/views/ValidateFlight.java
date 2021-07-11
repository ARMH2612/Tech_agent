package views;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class ValidateFlight {

    @FXML
    private TextField ticketNum;

    @FXML
    private TextField childNum;

    @FXML
    private Text priceLabel;

    @FXML
    private Text ticketNumLabel;

    @FXML
    private Text childNumLabel;

    @FXML
    private Text discountLabel;

    @FXML
    private Text totalLabel;

    private String flightInfo;

    void initData(String flightInfo){
       this.flightInfo = flightInfo;
    }

    public void validateClick(){

        String tab[] = flightInfo.split(" - ");

        priceLabel.setText("Price : "+tab[2]);
        ticketNumLabel.setText("Number Of Tickets : "+ticketNum.getText());
        childNumLabel.setText("Number of children under 7 : "+childNum.getText());
        if (!childNum.getText().equals("0")){
            discountLabel.setText("Discount : 10% for each");
        }else{
            discountLabel.setText("Discount : No Discount");
        }

        float total = 0;
        int iterNum = Integer.valueOf(ticketNum.getText());
        int discNum = Integer.valueOf(childNum.getText());

        total = Integer.valueOf(tab[2]) * (iterNum-discNum);
        for (int i = 0; i < discNum; i++) {
            total += (Float.valueOf(tab[2]) * 0.01);
        }

        totalLabel.setText("Total : "+total+" Da");
    }


    public void addToCart(){
        try {


            BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\user\\Desktop\\Tech_agent_tickets\\src\\views\\user_profile.txt", true));
            writer.append(' ');
            writer.append(priceLabel.getText()+","+ticketNumLabel.getText()+","+childNumLabel.getText()+","+discountLabel.getText()+","+totalLabel.getText()+"\n");

            writer.close();

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
