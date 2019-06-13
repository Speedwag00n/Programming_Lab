package resources.lang;

import java.util.ListResourceBundle;

public class lang_es_HN extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return contents;
    }

    private Object[][] contents = {

            {"header_login_button", "Iniciar sesión"},
            {"header_registration_button", "Registro"},
            {"header_exit_button", "Salida"},
            {"login_pane_title", "Ventana de autorización"},
            {"login_pane_login_label", "Iniciar sesión"},
            {"login_pane_password_label", "Contraseña"},
            {"login_pane_login_button", "Iniciar sesión"},
            {"login_pane_login_hint", "Escribir login y contraseña. Después de pulsar \"Iniciar sesión\""},

            {"registration_pane_title", "Ventana de registro"},
            {"registration_pane_login_label", "Iniciar sesión"},
            {"registration_pane_email_label", "Email"},
            {"registration_pane_register_button", "Registrarse"},
            {"registration_pane_register_hint", "Escribir login y correo electrónico. Después presiona \"Registrarse\""},
            {"registration_pane_login_hint", "El inicio de sesión debe tener menos de 30 símbolos y contener solo a-z, A-Z y 0-9 símbolos"},

            {"settings_pane_title", "Ventana de configuración"},
            {"settings_lang_combo_box", "Idioma"},
            {"settings_pane_port_label", "Puerto del servidor"},
            {"settings_pane_apply_button", "Aplicar"},
            {"settings_applied", "Configuración aplicada"},
            {"settings_negative_port", "¡El puerto no puede ser negativo!"},
            {"settings_to_big_port", "El puerto debe ser inferior a 65535 o igual!"},

            {"header_logout_button", "Cerrar sesión"},
            {"header_greetings_label", "Bienvenido,",},

            {"location_creation_pane_title", "Ventana de creación de objetos"},
            {"location_creation_pane_name_label", "Nombre de ubicación"},
            {"location_creation_pane_bottom_left_point_label", "Punto inferior izquierdo"},
            {"location_creation_pane_top_right_label", "Punto superior derecho"},
            {"location_creation_x_label", "X"},
            {"location_creation_y_label", "Y"},
            {"location_creation_pane_next_button", "Siguiente"},
            {"location_creation_pane_creation_hint", "Ponga valores en todos los campos y presione \"Siguiente\""},

            {"items_list_pane_title", "Artículos agregados"},

            {"items_list_table_header_name", "Nombre del elemento"},

            {"item_creation_pane_title", "Ventana de creación de elementos"},
            {"item_creation_pane_back_button", "Espalda"},
            {"item_creation_pane_add_button", "Añadir"},
            {"item_creation_pane_create_button", "Crear"},
            {"item_creation_pane_update_button", "Actualizar"},

            {"piece_creation_pane_name_label", "Nombre del elemento"},
            {"piece_creation_pane_weight_label", "Peso"},

            {"rock_creation_pane_name_label", "Nombre del elemento"},
            {"rock_creation_pane_stone_weight_label", "Peso de la piedra"},
            {"rock_creation_pane_ore_weight_label", "Peso del mineral"},

            {"mining_instrument_creation_pane_name_label", "Nombre del elemento"},
            {"mining_instrument_creation_pane_coefficient_label", "Coeficiente"},

            {"hammer_creation_pane_name_label", "Nombre del elemento"},
            {"hammer_creation_pane_head_material_label", "Material de la cabeza"},
            {"hammer_creation_pane_handle_material_label", "Material del mango"},

            {"update_location_pane_title", "Ventana de actualización de objeto"},
            {"update_location_pane_creation_hint", "Reescriba los parámetros de ubicación y haga clic en el siguiente"},

            {"locations_list_table_title", "Objetos existentes"},
            {"locations_list_table_header_name", "Nombre de ubicación"},
            {"locations_list_table_header_area", "Zona"},
            {"locations_list_table_header_position", "Posición"},
            {"locations_list_table_header_date", "Fecha de creación"},
            {"locations_list_table_header_items", "Artículos"},
            {"locations_list_table_header_owner", "Propietario"},

            {"items_list_table_title", "Objetos de objeto"},
            {"items_list_table_header_type", "Tipo"},
            {"items_list_table_header_params", "Parámetros"},

            {"login_form_model_invalid_login", "Iniciar sesión inválido!"},
            {"login_form_model_invalid_password", "¡Contraseña no válida!"},

            {"registration_form_model_invalid_login", "Inicio de sesión no válido"},
            {"registration_form_model_invalid_email", "¡Correo electrónico no válido!"},
            {"registration_form_model_starts_authorization", "Registro en proceso"},

            {"views_menu_table", "Mesa"},
            {"views_menu_creation", "Creación"},
            {"views_menu_visualisation", "Visualización"},
            {"views_menu_menu", "Menú de navegación"},

            {"location_creation_model_swapped_x", "¡La coordenada X del punto inferior izquierdo es más grande que la coordenada X del punto superior derecho!"},
            {"location_creation_model_equals_x", "la coordenada X de la esquina inferior izquierda y la coordenada X del punto superior derecho son iguales!"},
            {"location_creation_model_swapped_y", "¡La coordenada Y del punto inferior izquierdo es mayor que la coordenada Y del punto superior derecho!"},
            {"location_creation_model_equals_y", "La coordenada Y de la esquina inferior izquierda y la coordenada Y del punto superior derecho son iguales!"},
            {"location_creation_model_empty_name", "¡El campo de nombre está vacío!"},

            {"items_menu_piece", "Trozo"},
            {"items_menu_rock", "Rock"},
            {"items_menu_mining_instrument", "Instrumento minero"},
            {"items_menu_hammer", "Martillo"},

            {"piece_invalid_weight", "¡El peso de la pieza no es un flotador!"},
            {"piece_negative_weight", "¡El peso de la pieza no puede ser negativo!"},
            {"rock_invalid_stone_weight", "¡El peso de la piedra no es un flotador!"},
            {"rock_negative_stone_weight", "¡El peso de la piedra no puede ser negativo!"},
            {"rock_invalid_ore_weight", "¡El peso mineral de la roca no es un flotador!"},
            {"rock_negative_ore_weight_ore", "¡El peso del mineral no puede ser negativo!"},
            {"mining_instrument_invalid_coefficient", "¡El coeficiente del instrumento de minería no es un flotador!"},
            {"mining_instrument_coefficient_out_of_diapason", "¡El coeficiente del instrumento de minería está fuera del diapasón 0 y 1!"},
            {"hammer_invalid_head_material", "¡Seleccionar material de cabeza!"},
            {"hammer_invalid_handle_material", "¡Seleccione el material de la manija!"},

            {"object_item_rock_stone_weight", "Peso de la piedra"},
            {"object_item_rock_ore_weight", "Peso del mineral"},
            {"object_item_piece_weight", "Peso"},
            {"object_item_mininginstrument_coefficient", "Coeficiente"},
            {"object_item_hammer_head_name", "Material del encabezado"},
            {"object_item_hammer_handle_name", "Manejar material"},

            {"logout_message", "Saliste del sistema"},
            {"logout_not_authorized", "Parece que alguien está usando esta cuenta"},
            {"logout_no_response", "Parece que el servidor está deshabilitado ahora. Inténtelo más tarde"},
            {"location_creation_successful_added", "Ubicación exitosa agregada"},

            {"window_title_login_page", "Aplicación de servicio de objetos (página de inicio de sesión)"},
            {"window_title_registration_page", "Aplicación de servicio de objetos (página de registro)"},
            {"window_title_location_creation_page", "Aplicación de servicio de objetos (página de creación de ubicación)"},
            {"window_title_location_update_page", "Aplicación de servicio de objetos (página de actualización de ubicaciones)"},
            {"window_title_locations_table_page", "Aplicación de servicio de objetos (página de tabla de ubicaciones)"},
            {"window_title_visualisation_page", "Aplicación de servicio de objetos (página de visualización)"},
            {"window_title_settings_page", "Aplicación de servicio de objetos (página de configuración)"},
            {"window_title_items_adding_page", "Aplicación de servicio de objetos (elementos que agregan página)"},
            {"window_title_items_table_page", "Aplicación de servicio de objetos (página de tabla de elementos)"},

            {"location_filer_pane_field_list_label", "Campo"},
            {"location_filer_pane_field_list_name", "Nombre de ubicación"},
            {"location_filer_pane_field_list_area", "Zona"},
            {"location_filer_pane_field_list_position", "Posición"},
            {"location_filer_pane_field_list_items", "Artículos"},
            {"location_filer_pane_field_list_creation_date", "Fecha de creación"},
            {"location_filer_pane_field_list_owner", "Propietario"},

            {"items_filter_pane_field_list_title", "Campo"},
            {"items_filter_pane_field_list_type", "Tipo"},
            {"items_filter_pane_field_list_name", "Nombre"},

            {"head_material_combo_box_wood", "Madera"},
            {"head_material_combo_box_iron", "Planchar"},
            {"head_material_combo_box_steel", "Acero"},
            {"head_material_combo_box_plastic", "El plastico"},
            {"head_material_combo_box_stone", "Piedra"},

            {"items_table_type_cell_piece", "Trozo"},
            {"items_table_type_cell_rock", "Rock"},
            {"items_table_type_cell_mininginstrument", "Instrumento minero"},
            {"items_table_type_cell_hammer", "Martillo"},

            {"location_table_empty_placeholder", "No hay ubicaciones"},
            {"items_table_empty_placeholder", "No hay elementos"},
            {"items_list_empty_placeholder", "No hay elementos"},

            {"context_menu_show", "Espectáculo"},
            {"context_menu_edit", "Editar"},
            {"context_menu_delete", "Borrar"},

            {"creation_pane_image_chooser", "Elegir imagen"},
            {"icon_invalid_format", "Formato de imagen no soportado!"}

    };

}
