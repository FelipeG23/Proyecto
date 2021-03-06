/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Conexion.ConexionDAO;
import Entities.EdificioEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Felipe
 */
public class EdificioDAO {

    public String insertarEdificio(String nombreEdificio) {
        String rta = "";
        PreparedStatement ps = null;
        Connection conn = null;
        try {
            conn = ConexionDAO.GetConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("    INSERT INTO CLASE.EDIFICIOS (CODIGO, NOMBRE, ESTADO)        ");
            sql.append("         VALUES ((SELECT NVL(MAX(CODIGO)+1,1) FROM EDIFICIOS),   ");
            sql.append("                    ?,                                          ");
            sql.append("                   'A'                                          ");
            sql.append("                     )                                          ");
            ps = conn.prepareStatement(sql.toString());
            ps.setString(1, nombreEdificio.toUpperCase());
            ps.executeUpdate();
            rta = "OK";
        } catch (Exception e) {
            rta = "Error" + e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rta;
    }

    public String actualizarEdificio(EdificioEntity edificio) {
        String rta = "";
        PreparedStatement ps = null;
        Connection conn = null;
        try {
            conn = ConexionDAO.GetConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("            UPDATE CLASE.EDIFICIOS ");
            sql.append("              SET NOMBRE = ?       ");
            sql.append("             WHERE CODIGO = ?      ");
            ps = conn.prepareStatement(sql.toString());
            ps.setString(1, edificio.getNombre());
            ps.setLong(2, edificio.getCodigo());
            ps.executeUpdate();
            rta = "OK";
        } catch (Exception e) {
            rta = "Error" + e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rta;
    }

    public List<EdificioEntity> consultarEdificios() {
        List<EdificioEntity> lista = null;
        PreparedStatement ps = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            conn = ConexionDAO.GetConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("    SELECT CODIGO, NOMBRE, ESTADO FROM CLASE.EDIFICIOS  WHERE  ESTADO = 'A'  ");
            ps = conn.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                if (lista == null) {
                    lista = new ArrayList<>();
                }
                EdificioEntity aux = new EdificioEntity();
                aux.setCodigo(rs.getLong("CODIGO"));
                aux.setNombre(rs.getString("NOMBRE"));
                aux.setEstado(rs.getString("ESTADO"));
                lista.add(aux);
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
        return lista;
    }

    public String eliminarEdificio(Long idEdificio) {
        Connection conn = null;
        String rta = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Boolean flag = false;
        try {
            StringBuilder sql = new StringBuilder();
            conn = ConexionDAO.GetConnection();
            sql.append("        SELECT COUNT(*) FROM EDIFICIOS EDIF                      ");
            sql.append("        WHERE CODIGO IN (                                        ");
            sql.append("            SELECT  EDI_CODIGO FROM SALONES WHERE EDI_CODIGO = ? ");
            sql.append("        )                                                        ");
            sql.append("                                                                 ");
            ps = conn.prepareStatement(sql.toString());
            ps.setLong(1, idEdificio);
            rs = ps.executeQuery();
            while (rs.next()) {
                flag = rs.getInt(1) > 0;
            }
            if (!flag) {
                sql = new StringBuilder();
                sql.append("    UPDATE EDIFICIOS      ");
                sql.append("       SET ESTADO = 'I'   ");
                sql.append("     WHERE CODIGO = ?     ");
                ps = conn.prepareStatement(sql.toString());
                ps.setLong(1, idEdificio);
                ps.executeUpdate();
                rta = "OK";
            }else{
                rta = "El edificio tiene salones anidados";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rta;
    }

    public EdificioEntity consultarEdificioxId(Long idEdificio) {
        EdificioEntity rta = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = ConexionDAO.GetConnection();
            StringBuilder sql = new StringBuilder();
            sql.append("    SELECT CODIGO, NOMBRE, ESTADO FROM CLASE.EDIFICIOS              ");
            sql.append("    WHERE CODIGO = ?                                                ");
            ps = conn.prepareStatement(sql.toString());
            ps.setLong(1, idEdificio);
            rs = ps.executeQuery();
            while (rs.next()) {
                rta = new EdificioEntity();
                rta.setCodigo(rs.getLong("CODIGO"));
                rta.setNombre(rs.getString("NOMBRE"));
                rta.setEstado(rs.getString("ESTADO"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rta;
    }
}
