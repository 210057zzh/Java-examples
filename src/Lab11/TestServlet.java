package Lab11;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(urlPatterns = "/FirstServlet", name = "apple")
public class TestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public TestServlet() {
        super();
        System.out.println("in constructor");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("in init");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Gson gsonObj = new Gson();
        Map<String, String[]> allMap = request.getParameterMap();
        for (Object key : ((Map<?, ?>) allMap).keySet()) {
            String[] strArr = (String[]) allMap.get(key);
            for (String val : strArr) {
                System.out.println("Str Array= " + val);
            }
        }
        String jsonStr = gsonObj.toJson(allMap);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(jsonStr);
        out.flush();
        System.out.println("sent Json");
    }
}
