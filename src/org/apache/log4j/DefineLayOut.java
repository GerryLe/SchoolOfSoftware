package org.apache.log4j;
import org.apache.log4j.HTMLLayout;

public class DefineLayOut extends HTMLLayout {
	public String getContentType() {
		return "text/html;charset=GBK";
	}
}
