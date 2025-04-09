package edu.hsog.db;

import javax.swing.*;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.Stack;

public class DBQueries {



    static public int count() {
        int count = 0;
        ResultSet rs = null;
        Statement st = null;
        Connection con = Globals.getPoolConnection();
        try {
            st = con.createStatement();
            String q = "SELECT COUNT(*) FROM gadgets";
            System.out.printf(q);
            rs = st.executeQuery(q);
            rs.next();
            count = rs.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs!=null) rs.close();
                if (st!=null) st.close();
                if (con!=null)con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return count;
    }

    static public boolean login(String email, String passwd) {
        int i = 0;
        ResultSet rs = null;
        Statement st = null;
        Connection con = Globals.getPoolConnection();
        try {
            st = con.createStatement();
            String q = "select count (*)\n" +
                    "from users\n" +
                    "where email = '" +  email + "' and passwd = '" + passwd + "'"; // sql abfrage
            System.out.printf(q);
            rs = st.executeQuery(q);
            rs.next();
            i = rs.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs!=null) rs.close();
                if (st!=null) st.close();
                if (con!=null)con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return (i ==1);
    }


    public static Object[] getFirst() {
        Statement st = null;
        ResultSet rs = null;
        Object[] result;
        Connection con = Globals.getPoolConnection();

        try {
            st = con.createStatement();
            /*
            String q = "SELECT *\n" +
                    "FROM ( SELECT * FROM GADGETS ORDER BY url)\n" +
                    "WHERE ROWNUM = 1\n";

             */

            String q = "SELECT * FROM GADGETS ORDER BY url";

            System.out.println(q);
            rs = st.executeQuery(q);
            rs.next();

            result = new Object[] {rs.getObject(1), rs.getObject(2), rs.getObject(3), rs.getObject(4), rs.getBlob(5)};

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public static Object[] getLast() {
        Statement st = null;
        ResultSet rs = null;
        Object[] result;
        Connection con = Globals.getPoolConnection();

        try {
            st = con.createStatement();
            /*
            String q = "SELECT *\n" +
                    "FROM ( SELECT * FROM GADGETS ORDER BY url DESC)\n" +
                    "WHERE ROWNUM = 1";
            */

            String q = "SELECT * FROM GADGETS ORDER BY url desc";

            System.out.println(q);
            rs = st.executeQuery(q);
            rs.next();

            result = new Object[] {rs.getObject(1), rs.getObject(2), rs.getObject(3), rs.getObject(4), rs.getBlob(5)};

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public static Object[] getNext(String link) {
        Statement st = null;
        ResultSet rs = null;
        Object[] result;
        Connection con = Globals.getPoolConnection();

        try {

            if (link.equals(getLast()[0])) {
                result = getLast();
            }
            else {
                st = con.createStatement();
                String q = "SELECT *\n" +
                        "FROM ( SELECT * FROM GADGETS ORDER BY url)\n" +
                        "WHERE url > '" + link + "'";

                System.out.println(q);
                rs = st.executeQuery(q);
                rs.next();

                result = new Object[]{rs.getObject(1), rs.getObject(2), rs.getObject(3), rs.getObject(4), rs.getBlob(5)};
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public static Object[] getPrevious(String link) {
        Statement st = null;
        ResultSet rs = null;
        Object[] result;
        Connection con = Globals.getPoolConnection();

        try {
            if (link.equals(getFirst()[0])) {
                result = getFirst();
            }
            else {
                st = con.createStatement();
                String q = "SELECT *\n" +
                        "FROM ( SELECT * FROM GADGETS ORDER BY url DESC)\n" +
                        "WHERE url < '" + link + "'";

                System.out.println(q);
                rs = st.executeQuery(q);
                rs.next();

                result = new Object[]{rs.getObject(1), rs.getObject(2), rs.getObject(3), rs.getObject(4), rs.getBlob(5)};
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public static void saveItem(String link, String email, String keywords, String description, Icon cover, boolean inDB) {
        PreparedStatement st = null;
        Connection con = Globals.getPoolConnection();

        try {
            if (!inDB) {
            String q = "INSERT INTO gadgets\n" +
                    "(url, email, keywords, description, cover)\n" +
                    "VALUES\n" +
                    "( ?, ?, ?, ? ,?)";

                Blob b = Converter.icon2Blob(cover, con);
                System.out.println("Blob =" +b);

                st = con.prepareStatement(q);
                st.setString(1, link);
                st.setString(2, email);
                st.setString(3, keywords);
                st.setString(4, description);
                if (b != null) {
                    st.setBlob(5, b);
                } else {
                    st.setBlob(5, (Blob) null);
                }
                st.executeUpdate();
            }
            else {
            String q2 = "UPDATE gadgets SET keywords = ?, description = ?, cover = ? WHERE url = ? AND email = ?";

            Blob b = Converter.icon2Blob(cover, con);
            System.out.println("Blob =" +b);

            st = con.prepareStatement(q2);
            st.setString(1, keywords);
            // st.setString(5, email);
            st.setString(2, description);

            if (b != null) {
                st.setBlob(3, b);
            } else {
                st.setBlob(3, (Blob) null);
            }
            st.setString(4, link);
            st.setString(5, email);

            st.executeUpdate(); }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean checkIFInDB(String link) {
        Statement st = null;
        ResultSet rs = null;
        int result;
        Connection con = Globals.getPoolConnection();

        try {
            st = con.createStatement();
            String q = "SELECT Count (*)\n" +
                    "FROM gadgets\n" +
                    "WHERE url = '" + link + "'";

            System.out.println(q);
            rs = st.executeQuery(q);
            rs.next();

            result = rs.getInt(1);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return (result == 1);

    }

    public static boolean checkIFComment(String link, String email) {
        PreparedStatement st = null;
        ResultSet rs = null;
        Connection con = Globals.getPoolConnection();
        boolean exists = false;

        try {
            String q = "SELECT COUNT(*) FROM bewertung WHERE url = ? AND email = ?";
            System.out.println("Query: " + q);

            st = con.prepareStatement(q);
            st.setString(1, link);
            st.setString(2, email);

            rs = st.executeQuery();
            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("SQL-Fehler: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException("Fehler beim Schließen der Ressourcen.", e);
            }
        }

        return exists;
    }



    public static void addCommentAndRating(String link, String email, int gefallen, String comment) {
        PreparedStatement ps = null;
        Connection con = Globals.getPoolConnection();

        try {

            boolean exists = checkIFComment(link, email);
            System.out.println("Existiert Kommentar? " + exists);

            if (exists) {
                String q = "Update bewertung set gefallen = ?, kommentar = ? WHERE URL = ? AND email = ?";


                ps = con.prepareStatement(q);
                ps.setInt(1, gefallen);
                ps.setString(2, comment);
                ps.setString(3, link);
                ps.setString(4, email);

                ps.executeUpdate();

            }

            else{
                String q = "INSERT INTO bewertung \n" +
                        "(email, url, gefallen, kommentar)\n" +
                        "VALUES (?, ?, ?, ?)";

                ps = con.prepareStatement(q);

                ps.setString(1, email);
                ps.setString(2, link);
                ps.setInt(3, gefallen);
                ps.setString(4, comment);

                System.out.println(q);
                ps.executeUpdate();
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }



    public static String getComments(String link) {
        if (link == null || link.trim().isEmpty()) {
            throw new IllegalArgumentException("Link darf nicht null oder leer sein.");
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = Globals.getPoolConnection();
        StringBuilder result = new StringBuilder();

        try {

            String q = "SELECT kommentar FROM bewertung WHERE url = ? order by kommentar";
            ps = con.prepareStatement(q);


            ps.setString(1, link);


            System.out.println("Ausgeführte Query: " + ps.toString());
            // Query ausführen
            rs = ps.executeQuery();


            if (!rs.isBeforeFirst()) {
                return "Keine Kommentare gefunden.";
            }


            while (rs.next()) {
                result.append("- ").append(rs.getString(1)).append("\n");
            }

        } catch (SQLException e) {
            throw new RuntimeException("SQL-Fehler: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException("Fehler beim Schließen der Ressourcen.", e);
            }
        }

        return result.toString();
    }

    public static String getRating(String link) {
        if (link == null || link.trim().isEmpty()) {
            throw new IllegalArgumentException("Link darf nicht null oder leer sein.");
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = Globals.getPoolConnection();
        double gefallen = 0;

        try {

            String q = "SELECT AVG(gefallen) FROM bewertung WHERE url = ?";
            ps = con.prepareStatement(q);


            ps.setString(1, link);


            System.out.println("Ausgeführte Query: " + ps.toString());
            // Query ausführen
            rs = ps.executeQuery();
            rs.next();
            gefallen = rs.getDouble(1);


        } catch (SQLException e) {
            throw new RuntimeException("SQL-Fehler: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException("Fehler beim Schließen der Ressourcen.", e);
            }
        }

        return String.format("%.1f", gefallen);
    }


    public static void delete(String link) {
        PreparedStatement st = null;
        ResultSet rs = null;
        Connection con = Globals.getPoolConnection();
        boolean exists = false;

        try {
            String q = "DELETE FROM gadgets WHERE url = ?";
            System.out.println("Query: " + q);

            st = con.prepareStatement(q);
            st.setString(1, link);

            rs = st.executeQuery();

        } catch (SQLException e) {
            throw new RuntimeException("SQL-Fehler: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException("Fehler beim Schließen der Ressourcen.", e);
            }
        }

    }
}


