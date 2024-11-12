package org.arle.proyectofx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.arle.proyectofx.model.Producto;
import org.arle.proyectofx.dao.ProductoDAO;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProductoController {
    @FXML private TextField idField;
    @FXML private TextField nombreField;
    @FXML private TextField precioField;
    @FXML private TextField stockField;

    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, Long> colId;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;

    private ProductoDAO productoDAO;
    private ObservableList<Producto> productos;

    @FXML
    public void initialize() {
        productoDAO = new ProductoDAO();
        productos = FXCollections.observableArrayList();

        // Configurar las columnas
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // Configurar formato de números para la columna de precio
        colPrecio.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double precio, boolean empty) {
                super.updateItem(precio, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", precio));
                }
            }
        });

        tablaProductos.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        mostrarProducto(newSelection);
                    }
                }
        );

        actualizarTabla();
    }

    @FXML
    private void handleNuevo() {
        limpiarCampos();
    }

    @FXML
    private void handleGuardar() {
        try {
            Producto producto = new Producto();
            if (!idField.getText().isEmpty()) {
                producto.setId(Long.parseLong(idField.getText()));
            }
            producto.setNombre(nombreField.getText());
            producto.setPrecio(Double.parseDouble(precioField.getText()));
            producto.setStock(Integer.parseInt(stockField.getText()));

            if (producto.getId() == null) {
                productoDAO.guardar(producto);
            } else {
                productoDAO.actualizar(producto);
            }

            actualizarTabla();
            limpiarCampos();
            mostrarAlerta("Éxito", "Producto guardado correctamente", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Por favor, ingrese valores numéricos válidos", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar el producto: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleEliminar() {
        Producto productoSeleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar eliminación");
            alert.setHeaderText(null);
            alert.setContentText("¿Está seguro que desea eliminar este producto?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                productoDAO.eliminar(productoSeleccionado);
                actualizarTabla();
                limpiarCampos();
                mostrarAlerta("Éxito", "Producto eliminado correctamente", Alert.AlertType.INFORMATION);
            }
        } else {
            mostrarAlerta("Error", "Por favor, seleccione un producto", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleLimpiar() {
        limpiarCampos();
    }

    private void actualizarTabla() {
        productos.clear();
        productos.addAll(productoDAO.listarTodos());
        tablaProductos.setItems(productos);
    }

    private void limpiarCampos() {
        idField.clear();
        nombreField.clear();
        precioField.clear();
        stockField.clear();
        tablaProductos.getSelectionModel().clearSelection();
    }

    private void mostrarProducto(Producto producto) {
        idField.setText(producto.getId().toString());
        nombreField.setText(producto.getNombre());
        precioField.setText(producto.getPrecio().toString());
        stockField.setText(producto.getStock().toString());
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
