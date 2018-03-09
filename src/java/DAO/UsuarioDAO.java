/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Conexion.ConexionDAO;
import Entities.PersonaEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 *
 * @author Felipe
 */
public class UsuarioDAO {

    public String insertarPersona(JSONObject json) {
        Connection conn = null;
        PreparedStatement ps = null;
        String rta = "";
        try {
            Integer edad = json.getInt("edad");
            conn = ConexionDAO.GetConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO propietario (nombre, apellidos, edad) VALUES (?,?,?) ");
            ps = conn.prepareCall(sql.toString());
            ps.setString(1, json.getString("nombre"));
            ps.setString(2, json.getString("apellidos"));
            ps.setInt(3, edad);
            ps.executeUpdate();
            rta = "OK";
        } catch (Exception e) {
            e.printStackTrace();
            rta = e.getMessage();
        } finally {
            try {
                if (null != conn) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return rta;
    }

    public ArrayList<PersonaEntity> consultarTodasPersonas() {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        ArrayList<PersonaEntity> rta = null;
        try {

            conn = ConexionDAO.GetConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT id ,nombre, apellidos, edad FROM propietario ORDER BY ID ASC");
            st = conn.createStatement();
            rs = st.executeQuery(sql.toString());
            while (rs.next()) {
                if (null == rta) {
                    rta = new ArrayList<>();
                }
                PersonaEntity aux = new PersonaEntity();
                aux.setId(rs.getString("ID"));
                aux.setNombre_P(rs.getString("nombre"));
                aux.setApellido(rs.getString("apellidos"));
                aux.setEdad(rs.getInt("edad"));
                rta.add(aux);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (null != conn) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return rta;
    }

    public PersonaEntity consultarEspecifico(String id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PersonaEntity respuesta = null;
        try {
            conn = ConexionDAO.GetConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT NOMBRE,APELLIDOS,EDAD FROM PROPIETARIO WHERE ID = ? ");
            ps = conn.prepareStatement(sql.toString());
            ps.setInt(1, Integer.parseInt(id));
            rs = ps.executeQuery();
            while (rs.next()) {
                respuesta = new PersonaEntity();
                respuesta.setNombre_P(rs.getString("NOMBRE"));
                respuesta.setApellido(rs.getString("APELLIDOS"));
                respuesta.setEdad(rs.getInt("EDAD"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != conn) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return respuesta;
    }
    
    public String actualizar(PersonaEntity persona) {
        Connection conn = null;
        PreparedStatement ps = null;
        String respuesta = "";
        try {
            conn = ConexionDAO.GetConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE PROPIETARIO  ");
            sql.append("SET NOMBRE = ? ,APELLIDOS = ? ,EDAD = ? WHERE ID = ?");
            ps = conn.prepareStatement(sql.toString());
            ps.setString(1, persona.getNombre_P());
            ps.setString(2, persona.getApellido());
            ps.setInt(3, persona.getEdad());
            ps.setInt(4, Integer.parseInt(persona.getId()));
            ps.executeUpdate();
            respuesta = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != conn) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return respuesta;
    }
    public String eliminar(String id) {
        Connection conn = null;
        PreparedStatement ps = null;
        String respuesta = "";
        try {
            conn = ConexionDAO.GetConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("DELETE FROM PROPIETARIO  ");
            sql.append("WHERE ID = ?");
            ps = conn.prepareStatement(sql.toString());
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();
            respuesta = "OK";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != conn) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return respuesta;
    }
}
