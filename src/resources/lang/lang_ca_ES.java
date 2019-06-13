package resources.lang;

import java.util.ListResourceBundle;

public class lang_ca_ES extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return contents;
    }

    private Object[][] contents = {

            {"header_login_button", "Iniciar Sessió"},
            {"header_registration_button", "Registre"},
            {"header_exit_button", "Sortir"},
            {"login_pane_title", "Finestra d'autorització"},
            {"login_pane_login_label", "Iniciar Sessió"},
            {"login_pane_password_label", "Contrasenya"},
            {"login_pane_login_button", "Iniciar sessió"},
            {"login_pane_login_hint", "Escriviu l’accés i la contrasenya. Després de prémer \"Inicia la sessió\""},

            {"registration_pane_title", "Ginestra de registre"},
            {"registration_pane_login_label", "Iniciar Sessió"},
            {"register_pane_email_label", "Correu electrònic"},
            {"registration_pane_register_button", "Inicieu la sessió"},
            {"registration_pane_register_hint", "Escriviu login i correu electrònic. Després de prémer \"Inicieu la sessió\""},
            {"registration_pane_login_hint", "El registre ha de ser inferior a 30 símbols i només pot contenir símbols a-z, A-Z i 0-9"},

            {"settings_pane_title", "Finestra de configuració"},
            {"settings_lang_combo_box", "Idioma"},
            {"settings_pane_port_label", "Port del servidor"},
            {"settings_pane_apply_button", "Aplica"},
            {"settings_applied", "Configuració aplicada"},
            {"settings_negative_port", "El port no pot ser negatiu"},
            {"settings_to_big_port", "eEl port ha de ser inferior a 65535 o igual."},

            {"header_logout_button", "Tancar sessió"},
            {"header_greetings_label", "Benvingut,"},

            {"location_creation_pane_title", "fFinestra de creació d'objectes"},
            {"location_creation_pane_name_label", "Nom de la ubicació"},
            {"location_creation_pane_bottom_left_point_label", "Punt inferior esquerra"},
            {"location_creation_pane_top_right_label", "Punt superior"},
            {"location_creation_x_label", "X"},
            {"location_creation_y_label", "Y"},
            {"location_creation_pane_next_button", "Pròxim"},
            {"location_creation_pane_creation_hint", "Poseu valors a tots els camps i premeu \"Següent\""},

            {"items_list_pane_title", "Elements afegits"},

            {"items_list_table_header_name", "Nom de l’element"},

            {"item_creation_pane_title", "Finestra de creació d'elements"},
            {"item_creation_pane_back_button", "Esquena"},
            {"item_creation_pane_add_button", "Afegir"},
            {"item_creation_pane_create_button", "Crear"},
            {"item_creation_pane_update_button", "Actualització"},

            {"piece_creation_pane_name_label", "Nom de l'element"},
            {"piece_creation_pane_weight_label", "Pes"},

            {"rock_creation_pane_name_label", "Nom de l'element"},
            {"rock_creation_pane_stone_weight_label", "Pes de la pedra"},
            {"rock_creation_pane_ore_weight_label", "Pes del mineral"},

            {"mining_instrument_creation_pane_name_label", "Nom de l'element"},
            {"mining_instrument_creation_pane_coefficient_label", "Coeficient"},

            {"hammer_creation_pane_name_label", "Nom de l'element"},
            {"hammer_creation_pane_head_material_label", "Material principal"},
            {"hammer_creation_pane_handle_material_label", "Manejar material"},

            {"update_location_pane_title", "Finestra d'actualització d'objectes"},
            {"update_location_pane_creation_hint", "Torneu a escriure els paràmetres de la ubicació i feu clic a continuació"},

            {"locations_list_table_title", "Objectes existents"},
            {"locations_list_table_header_name", "Nom de la ubicació"},
            {"locations_list_table_header_area", "Àrea"},
            {"locations_list_table_header_position", "Posició"},
            {"location_list_table_header_date", "Data de creació"},
            {"locations_list_table_header_items", "Articles"},
            {"locations_list_table_header_owner", "Propietari"},

            {"items_list_table_title", "Articles d’objecte"},
            {"items_list_table_header_type", "Escriviu"},
            {"items_list_table_header_params", "Paràmetres"},

            {"login_form_model_invalid_login", "Accés incorrecte!"},
            {"login_form_model_invalid_password", "Contrasenya no vàlida"},

            {"registration_form_model_invalid_login", "Accés incorrecte!"},
            {"registration_form_model_invalid_email", "Correu electrònic no vàlid"},
            {"registration_form_model_starts_authorization", "Registre en procés"},

            {"views_menu_table", "Taula"},
            {"views_menu_creation", "Creació"},
            {"views_menu_visualisation", "Visualització"},
            {"views_menu_menu", "menú de navegació"},

            {"location_creation_model_swapped_x", "La coordenada X del punt inferior esquerre és més gran que la coordenada X del punt superior dret"},
            {"location_creation_model_equals_x", "La coordenada X de la part inferior esquerra i la coordenada X del punt superior dret són iguals!"},
            {"location_creation_model_swapped_y", "La coordenada Y del punt inferior esquerre és més gran que la coordenada Y del punt superior dret!"},
            {"location_creation_model_equals_y", "La coordenada Y de la part inferior esquerra i la coordenada Y del punt superior dret són iguals"},
            {"location_creation_model_empty_name", "El camp Nom està buit!"},

            {"elements_menu_piece", "Peça"},
            {"articles_menu_rock", "Rock"},
            {"elements_menu_mining_instrument", "Instrument de mineria"},
            {"articles_menu_hammer", "Martell"},

            {"piece_invalid_weight", "El pes de la peça no és un flotador!"},
            {"piece_negative_weight", "El pes de la peça no pot ser negatiu"},
            {"rock_invalid_stone_weight", "El pes de pedra de la roca no és un flotador"},
            {"rock_negative_stone_weight", "El pes de la pedra no pot ser negatiu"},
            {"rock_invalid_ore_weight", "El pes del mineral de la roca no és un flotador"},
            {"rock_negative_ore_weight", "El pes del mineral no pot ser negatiu"},
            {"mining_instrument_invalid_coefficient", "El coeficient de l'instrument de mineria no és un flotant"},
            {"mining_instrument_coefficient_out_of_diapason", "El coeficient de l'instrument de mineria és fora de diapason 0 i 1"},
            {"hammer_invalid_head_material", "Selecciona material principal"},
            {"hammer_invalid_handle_material", "Seleccioneu el material de maneig!"},

            {"object_item_rock_stone_weight", "Pes de pedra"},
            {"object_item_rock_ore_weight", "Pes del mineral"},
            {"object_item_piece_weight", "Pes"},
            {"object_item_mininginstrument_coefficient", "Coeficient"},
            {"object_item_hammer_head_name", "Material principal"},
            {"object_item_hammer_handle_name", "Manejar material"},

            {"logout_message", "Has sortit del sistema"},
            {"logout_not_authorized", "Sembla que algú està utilitzant aquest compte"},
            {"logout_no_response", "Ara sembla que el servidor està desactivat. Torna-ho a provar més tard"},
            {"location_creation_successful_added", "Ubicació afegida afegida"},

            {"window_title_login_page", "Aplicació del servei d'objectes (pàgina d'inici de sessió)"},
            {"window_title_registration_page", "Aplicació del servei d'objectes (pàgina de registre)"},
            {"window_title_location_creation_page", "Aplicació de servei d'objectes (pàgina de creació d'ubicacions)"},
            {"window_title_update_page", "Aplicació del servei d'objectes (pàgina d'actualització de llocs)"},
            {"window_title_locations_table_page", "Aplicació del servei d'objectes (pàgina de la taula d'ubicacions)"},
            {"window_title_visualisation_page", "Aplicació de servei d'objectes (pàgina de visualització)"},
            {"window_title_settings_page", "Aplicació de servei d'objectes (pàgina de configuració)"},
            {"window_title_items_adding_page", "Aplicació del servei d'objectes (articles que afegeixen pàgina)"},
            {"window_title_items_table_page", "Aplicació de servei d'objectes (pàgina de taula d'articles)"},

            {"location_filer_pane_field_list_label", "Camp"},
            {"location_filer_pane_field_list_name", "Nom de la ubicació"},
            {"location_filer_pane_field_list_area", "Àrea"},
            {"location_filer_pane_field_list_position", "Posició"},
            {"location_filer_pane_field_list_items", "Articles"},
            {"location_filer_pane_field_list_creation_date", "Date de creació"},
            {"location_filer_pane_field_list_owner", "Propietari"},

            {"articles_filter_pane_field_list_title", "Camp"},
            {"articles_filter_pane_field_list_type", "Escriviu"},
            {"item_filter_pane_field_list_name name", "Nom"},

            {"head_material_combo_box_wood", "Fusta"},
            {"head_material_combo_box_iron", "Ferro"},
            {"head_material_combo_box_steel", "Acer"},
            {"head_material_combo_box_plastic", "Plàstic"},
            {"head_material_combo_box_stone", "Pedra"},

            {"items_table_type_cell_piece", "Peça"},
            {"items_table_type_cell_rock", "Rock"},
            {"items_table_type_cell_mininginstrument", "Instrument de mineria"},
            {"items_table_type_cell_hammer", "Martell"},

            {"location_table_empty_placeholder", "No hi ha ubicacions"},
            {"items_table_empty_placeholder", "No hi ha elements"},
            {"items_list_empty_placeholder", "No hi ha elements"},

            {"context_menu_show", "Espectacle"},
            {"context_menu_edit", "Edita"},
            {"context_menu_delete", "Suprimeix"},

            {"creation_pane_image_chooser", "Trieu la imatge"},
            {"icon_invalid_format", "Format d’imatge no compatible"}

    };

}
