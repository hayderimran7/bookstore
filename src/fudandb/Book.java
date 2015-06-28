package fudandb;

import java.sql.ResultSet;
import java.sql.Statement;

public class Book {

	private String[] attrName = { "ISBN", "title", "author", "publisher",
			"publish_year", "copies", "price", "format", "keywords", "subject" };
	private String tableName = "book";

	public Book() {

	}

	public void initBook(Statement stmt) throws Exception {

		String[] attrValue;

		attrValue = new String[] { "'0133760065'",
				"'Computer Science: An Overview'",
				"'Glenn Brookshear,Dennis Brylow'", "'CMPEDU'", "'2011-10-11'",
				"20", "55.7", "'paper'", "'computer,introduction'",
				"'computer'" };
		this.newBook(attrValue, stmt);
		attrValue = new String[] {
				"'9787111407010'",
				"'Introduction to Algorithm'",
				"'Thomas H.Cormen,Charles E.Leiserson,Ronald L.Rivest,Clifford Stein'",
				"'CMPEDU'", "'2013-7-1'", "10", "73.20", "'paper'",
				"'algorithm,introduction'", "'computer'" };
		this.newBook(attrValue, stmt);
		attrValue = new String[] { "'9787500794486'",
				"'Never Go Back: (Jack Reacher 18)'", "'Lee Child'",
				"'Bantam'", "'2009-12-01'", "22", "55.0", "'paper'",
				"'Hero, Beauty'", "'literature'" };
		this.newBook(attrValue, stmt);
		attrValue = new String[] { "'515130044'", "'The Attorney'",
				"'Steve Martini'", "'Jove; Reissue'", "'2001-01-01'", "5",
				"98.0", "'paper'", "'Attorney'", "'literature'" };
		this.newBook(attrValue, stmt);
		attrValue = new String[] { "'747266093'", "'The Jury'",
				"'Steve Martini'", "'Headline Book Publishing; New Ed'",
				"'2001-10-04'", "99", "62.0", "'paper'", "'jury'",
				"'literature'" };
		this.newBook(attrValue, stmt);
		attrValue = new String[] { "'9780553841008'",
				"'The Affair: A Jack Reacher Novel'", "'Lee Child'", "'z.cn'",
				"'2012-03-27'", "2", "50.0", "'paper'", "'affair'",
				"'literature'" };
		this.newBook(attrValue, stmt);
	}

	public void newBook(String[] attrValue, Statement stmt) throws Exception {

		Common com = new Common();
		com.newTuple(attrValue, tableName, attrName, stmt);
	}

	public void deleteBook(String ISBN, Statement stmt) throws Exception {

		String query;

		query = "delete from book where book.ISBN=";
		query += "'" + ISBN + "';";
		System.out.println(query);

		try {
			stmt.execute(query);
		} catch (Exception e) {
			System.err.println("Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			throw (e);
		}

	}

	public void updateBook(String key, String keyValue, String[] attr,
			String[] value, Statement stmt) throws Exception {

		Common com = new Common();
		com.updateTuple(key, keyValue, attr, value, tableName, stmt);
	}

	public ResultSet showBook(String[] sigmaAttr, String[] sigmaValue,
			Statement stmt) throws Exception {

		Common com = new Common();
		return com.showTable(sigmaAttr, sigmaValue, tableName, stmt);

	}

	public ResultSet browseBook(String queryStmt, int sortType, String name,
			Statement stmt) throws Exception {

		String query;

		query = "select book.ISBN as ISBN, title, author, publisher, publish_year, copies, price, format, keywords,"
				+ "subject, t1.avg_rate as rate, t2.avg_rate as trust_rate "
				+ "from book left join ("
				+ "		select b.ISBN as ISBN1, avg(f.rate) as avg_rate "
				+ "		from book as b, feedback as f where b.ISBN = f.ISBN group by b.ISBN"
				+ "		) as t1 on book.ISBN=t1.ISBN1 left join ("
				+ "		select b.ISBN as ISBN2, avg(f.rate) as avg_rate "
				+ "		from book as b, feedback as f, customer_rate as c "
				+ "		where b.ISBN = f.ISBN and f.cid = c.cid2 and c.trusted=true and c.cid1="
				+ name + " " + "		group by b.ISBN) as t2 on book.ISBN=t2.ISBN2";
		if (queryStmt.length() != 0) {
			query += " where " + queryStmt;
		}
		switch (sortType) {
		case 0:
			break;
		case 1:
			query += " order by publish_year";
			break;
		case 2:
			query += " order by rate desc";
			break;
		case 3:
			query += " order by trust_rate desc";
			break;
		default:
			break;
		}
		query += ";";
		System.out.println(query);

		ResultSet results;
		try {
			results = stmt.executeQuery(query);
		} catch (Exception e) {
			System.err.println("Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			throw (e);
		}

		return results;

	}

	public int countBook(Statement stmt) throws Exception {

		Common com = new Common();
		int count = com.countTuple(tableName, stmt);
		return count;
	}

}