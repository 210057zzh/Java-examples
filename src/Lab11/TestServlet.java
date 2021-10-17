package csci201;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/FirstServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public TestServlet() {
		super();
		System.out.println("in constructor");
	}
	public void init(ServletConfig config) throws ServletException {
		System.out.println("in init");
	}
	protected void service(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		System.out.println("in service");
	}
}
