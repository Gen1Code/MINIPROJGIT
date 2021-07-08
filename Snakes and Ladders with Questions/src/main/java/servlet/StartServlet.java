package servlet;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

@WebServlet(
        name = "StartServlet",
        urlPatterns = {"/start"}
)
public class StartServlet extends HttpServlet{

    JSONObject data;
    JSONParser parser = new JSONParser();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
            try {
                Random rnd = new Random();
                FileReader fileR = new FileReader("src/main/webapp/data.json");
                data = (JSONObject) parser.parse(fileR);
                fileR.close();
                File file = new File("src/main/webapp/Questions.txt");
                Scanner myReader = new Scanner(file);
                int QNOo = rnd.nextInt(3) + 1;
                for (int i = 0; i < QNOo; i++) {
                    myReader.nextLine();
                }
                String[] question = myReader.nextLine().split(",");
                myReader.close();

                ((JSONObject) data.get("Question")).put("Q", question[0]);
                ((JSONObject) data.get("Question")).put("A", question[1]);
                ((JSONObject) data.get("Question")).put("AD", false);
                ((JSONObject) data.get("Question")).put("AC", false);

                FileWriter fileW = new FileWriter("src/main/webapp/data.json");
                fileW.write(data.toString());
                fileW.close();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        resp.getWriter().write(String.valueOf(data));

    }



}
