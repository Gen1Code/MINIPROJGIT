package servlet;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileReader;
import java.io.IOException;


@WebServlet(
        name = "UpdateServlet",
        urlPatterns = {"/update"}
)
public class UpdateServlet extends HttpServlet {

    JSONObject data;
    JSONParser parser = new JSONParser();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try {
            FileReader fileR = new FileReader("src/main/webapp/data.json");
            data = (JSONObject) parser.parse(fileR);
            fileR.close();
        }catch (ParseException e){
            e.printStackTrace();
        }

        resp.getWriter().write(String.valueOf(data));

    }

}
