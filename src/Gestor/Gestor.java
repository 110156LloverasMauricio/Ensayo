package Gestor;

import Modelos.Articulo;
import Modelos.DtoSumaPreciosRubro;
import Modelos.Rubro;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;

import java.util.ArrayList;

public class Gestor {

    private String CONN = "jdbc:sqlserver://DESKTOP-FOSHKGB;databaseName=Articulos";
    private String USER = "sa";
    private String PASS = "1234";
    private Connection con = null;

    public Gestor() {

    }

    private void abrirConexion() {
        try {
            
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(CONN, USER, PASS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void cerrarConexion() {
        try {

            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void borrar(int id) {

        try {
            abrirConexion();
            String sql = "delete articulos where idArticulo = ?";

            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            st.executeUpdate();
            st.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrarConexion();
        }

    }

    public Rubro obtenerRubroPorId(int id) {

        Rubro rubro = new Rubro();
        try {
            abrirConexion();
            String sql = "select * from Rubros where idRubro=?";
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String nombreRubro = rs.getString("nombreRubro");
                rubro = new Rubro(id, nombreRubro);
            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrarConexion();
        }

        return rubro;

    }

    public ArrayList<Articulo> obtenerTodos() {

        ArrayList<Articulo> lista = new ArrayList<>();
        try {
            abrirConexion();

            String sql = "select * from Articulos";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("idArticulo");
                String descripcion = rs.getString("descripcion");

                float precio = rs.getFloat("precio");
                int idRubro = rs.getInt("idRubro");

                Gestor g = new Gestor();
                Rubro rubro = g.obtenerRubroPorId(idRubro);

                Articulo a = new Articulo(id, descripcion, precio, rubro);
                lista.add(a);

            }
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            cerrarConexion();
        }
        return lista;
    }

    public ArrayList<Rubro> obtenerTodosPuestos() {

        ArrayList<Rubro> lista = new ArrayList<>();
        try {
            abrirConexion();
            String sql = "select * from Rubros";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int idRubro = rs.getInt("idRubro");
                String nombreRubro = rs.getString("nombreRubro");
                Rubro rubro = new Rubro(idRubro, nombreRubro);
                lista.add(rubro);
            }

            st.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrarConexion();
        }
        return lista;
    }

    public void agregarArticulo(Articulo a) {

        try {
            abrirConexion();
            String sql = "INSERT INTO Articulos (descripcion,precio, idRubro) values(?,?,?)";

            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, a.getDescripcion());
            st.setFloat(2, a.getPrecio());
            st.setInt(3, a.getRubro().getId());
            st.executeUpdate();
            st.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrarConexion();
        }

    }

    public float reportePrecioMax() {
        float resultado = -1;

        try {
            abrirConexion();
            String consulta = "select Max(precio) as MayorPrecio from articulos ";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            if (rs.next()) {
                resultado = rs.getFloat("MayorPrecio");
            }

            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrarConexion();
        }
        return resultado;
    }

    public ArrayList<DtoSumaPreciosRubro> reporteSumaDePreciosPorRubro() {
        ArrayList<DtoSumaPreciosRubro> resultado = new ArrayList<DtoSumaPreciosRubro>();

        try {
            abrirConexion();
            String sql = "select Sum(precio) as SumaPrecios, r.nombreRubro as Nombre from articulos as a join rubros as r\n"
                    + "on a.idRubro=r.idRubro\n"
                    + "group by a.idRubro , r.nombreRubro";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                float suma = rs.getFloat("SumaPrecios"); //uso los alias de columna
                String nombre = rs.getString("Nombre");
                DtoSumaPreciosRubro dto = new DtoSumaPreciosRubro(suma, nombre);
                resultado.add(dto);
            }

            st.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrarConexion();
        }

        return resultado;
    }

}//fin de clase gestor
