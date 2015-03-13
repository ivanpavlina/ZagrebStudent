package com.exitcode.zagrebstudent;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Parser {
	Document document;
	Elements elements;
	String resultText;

	public String Parse(int id, Document document) {
		int index;

		switch (id) {

		case 0:
			// Studentski dom Stjepan Radić
			elements = document.select("div[class=newsItem subpage]");
			resultText = elements.toString();
			resultText = resultText.replaceAll("</p>", "\n")
					.replaceAll("</li>", "\n").replaceAll("<h2>", "")
					.replaceAll("</h2>", "").replaceAll("</ul>", "").trim();

			break;

		case 1:
			// Studentski dom Šara
			elements = document.select("div[class=newsItem subpage]");
			resultText = elements.toString();
			resultText = resultText.replaceAll("</p>", "\n")
					.replaceAll("</li>", "\n").replaceAll("<h2>", "")
					.replaceAll("</h2>", "").replaceAll("</ul>", "").trim();

			break;

		case 2:
			// Studentski dom Cvjetno naselje
			elements = document.select("div[class=newsItem subpage]");
			resultText = elements.toString();
			resultText = resultText.replaceAll("</p>", "\n")
					.replaceAll("</li>", "\n").replaceAll("<h2>", "")
					.replaceAll("</h2>", "").replaceAll("</ul>", "").trim();

			break;

		case 3:
			// Studentski dom Lašćina
			elements = document.select("div[class=newsItem subpage]");
			resultText = elements.toString();
			resultText = resultText.replaceAll("</p>", "\n")
					.replaceAll("</li>", "\n").replaceAll("<h2>", "")
					.replaceAll("</h2>", "").replaceAll("</ul>", "")
					.replaceAll("<td>", "\n").replaceAll("<\td>", "\n").trim();

			break;

		case 4:
			// Restoran Savska
			elements = document.select("div[class=content]");
			resultText = elements.toString();

			index = resultText.indexOf("Ponuda se sastoji od:");

			resultText = resultText.replaceAll("<h3>", "<h2>")
					.replaceAll("</h3>", "</h2>")
					.replaceAll("<p>&nbsp;</p>", "").substring(index).trim();

			break;

		case 5:
			// Restoran Stjepan Radić
			elements = document.select("div[class=content]");
			resultText = elements.toString();

			index = resultText.indexOf("<strong>Restoran I");
			resultText = resultText.substring(index);

			break;

		case 6:
			// Restoran Cvjetno Naselje
			elements = document.select("div[class=content]");
			resultText = elements.toString();

			index = resultText.indexOf("Ponuda se sastoji od");
			resultText = resultText.substring(index);

			break;

		case 7:
			// Restoran Restoran Lašćina
			elements = document.select("div[class=content]");
			resultText = elements.toString();

			index = resultText.indexOf("Ponuda se sastoji od");
			resultText = resultText.substring(index);

			break;

		case 8:
			// Restoran Borongaj
			elements = document.select("div[class=content]");
			resultText = elements.toString();

			index = resultText.indexOf("Ponuda se sastoji od");
			resultText = resultText.substring(index);

			break;

		case 9:
			// Restoran Ekonomija
			elements = document.select("div[class=content]");
			resultText = elements.toString();

			index = resultText.indexOf("Restoran posjeduje konzumni prostor");
			resultText = resultText.substring(index);

			break;

		case 10:
			// Restoran Medicina
			elements = document.select("div[class=content]");
			resultText = elements.toString();

			index = resultText.indexOf("Ponuda se sastoji od");
			resultText = resultText.substring(index);

			break;

		case 11:
			// Restoran Veterina
			elements = document.select("div[class=content]");
			resultText = elements.toString();

			index = resultText.indexOf("Ponuda se sastoji od");
			resultText = resultText.substring(index);

			break;

		case 12:
			// Restoran Šumarstvo
			elements = document.select("div[class=content]");
			resultText = elements.toString();

			index = resultText.indexOf("Ponuda se sastoji od");
			resultText = resultText.substring(index);

			break;

		case 13:
			// Restoran FSB
			elements = document.select("div[class=content]");
			resultText = elements.toString();

			index = resultText.indexOf("Ponuda se sastoji od");
			resultText = resultText.substring(index);

			break;

		case 14:
			// Restoran ALU
			elements = document.select("div[class=content]");
			resultText = elements.toString();

			index = resultText.indexOf("Ponuda se sastoji od");
			resultText = resultText.substring(index);

			break;

		case 15:
			// Restoran TTF
			elements = document.select("div[class=content]");
			resultText = elements.toString();

			index = resultText.indexOf("Ponuda se sastoji od");
			resultText = resultText.substring(index);

			break;

		case 16:
			// Zagrebački Studentski Centar
			break;

		case 17:
			// Sisački Studentski Centar
			break;

		case 18:
			// Karlovački Studentski Centars
			break;

		}

		return resultText;
	}

}
