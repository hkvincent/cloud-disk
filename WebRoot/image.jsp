<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="image/jpeg"
	import="java.awt.*,java.awt.image.*,java.util.*,javax.imageio.*"%>
<%!//验证码
	//create different color for drawing line
	public Color getColor() {
		Random random = new Random();
		int r = random.nextInt(256);//0-255
		int g = random.nextInt(256);
		int b = random.nextInt(256);
		return new Color(r, g, b);
	}

	//create 4 number
	public String getNum() {
		String str = "";
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			str += random.nextInt(10);//0-9
		}
		return str;
	}%>
<%
	//this method is clearing the cache
	response.setHeader("pragma", "mo-cache");
	response.setHeader("cache-control", "no-cache");
	response.setDateHeader("expires", 0);
	//using image API,create a rectangle with TYPE_INT_RGB type and 80 30 size
	BufferedImage image = new BufferedImage(80, 30,
			BufferedImage.TYPE_INT_RGB);
	//Graphics class is for drawing string or anything you want in rectangle
	Graphics g = image.getGraphics();
	//the background color
	g.setColor(new Color(200, 200, 200));
	//set the size of text field
	g.fillRect(0, 0, 80, 30);

	//create 30 lines to draw
	for (int i = 0; i < 30; i++) {
		Random random = new Random();
		int x = random.nextInt(80);
		int y = random.nextInt(30);
		int xl = random.nextInt(x + 10);
		int yl = random.nextInt(y + 10);
		//each line color is different
		g.setColor(getColor());
		//location for drawing line
		g.drawLine(x, y, x + xl, y + yl);
	}

	//set the text font,fisrt is style second is bold,and third is size.
	g.setFont(new Font("serif", Font.BOLD, 16));
	//the text color
	g.setColor(Color.BLACK);
	String checkNum = getNum();//"2525"

	StringBuffer sb = new StringBuffer();
	//for adding space
	for (int i = 0; i < checkNum.length(); i++) {
		sb.append(checkNum.charAt(i) + " ");//"2 5 2 5"
	}
	//draw the "2 5 2 5" in to rectangle,and the start location is 15 20
	g.drawString(sb.toString(), 15, 20);
	//in jsp page we can directly get the session object
	//because they are packaged in pagecontext object
	session.setAttribute("CHECKNUM", checkNum);//"2525"

	//response.getOutputStream() is getting the same channel with request
	//using jpeg format to send image to user broswer who is broswering
	ImageIO.write(image, "jpeg", response.getOutputStream());
	//clear the cache in ImageIO
	out.clear();
	//for outputing other page
	//if second page want to use this image, then we need to use this statement
	out = pageContext.pushBody();
%>