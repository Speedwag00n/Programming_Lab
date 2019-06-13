package resources.lang;

import java.util.ListResourceBundle;

public class lang_en_EN extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return contents;
    }

    private Object[][] contents = {

            {"header_login_button", "Login"},
            {"header_registration_button", "Registration"},
            {"header_exit_button", "Exit"},
            {"login_pane_title", "Authorization window"},
            {"login_pane_login_label", "Login"},
            {"login_pane_password_label", "Password"},
            {"login_pane_login_button", "Log in"},
            {"login_pane_login_hint", "Write login and password. After press \"Log in\""},

            {"registration_pane_title", "Registration window"},
            {"registration_pane_login_label", "Login"},
            {"registration_pane_email_label", "Email"},
            {"registration_pane_register_button", "Sing in"},
            {"registration_pane_register_hint", "Write login and email. After press \"Sing in\""},
            {"registration_pane_login_hint", "Login needs to be shorter than 30 symbols and to contain only a-z, A-Z and 0-9 symbols"},

            {"settings_pane_title", "Settings window"},
            {"settings_lang_combo_box", "Language"},
            {"settings_pane_port_label", "Server port"},
            {"settings_pane_apply_button", "Apply"},
            {"settings_applied", "Settings applied"},
            {"settings_negative_port", "Port can't be negative!"},
            {"settings_to_big_port", "Port must be less than 65535 or equal it!"},

            {"header_logout_button", "Logout"},
            {"header_greetings_label", "Welcome,"},

            {"location_creation_pane_title", "Object creation window"},
            {"location_creation_pane_name_label", "Location name"},
            {"location_creation_pane_bottom_left_point_label", "Bottom-left point"},
            {"location_creation_pane_top_right_label", "Top-Right point"},
            {"location_creation_x_label", "X"},
            {"location_creation_y_label", "Y"},
            {"location_creation_pane_next_button", "Next"},
            {"location_creation_pane_creation_hint", "Put values into all fields and press \"Next\""},

            {"items_list_pane_title", "Added items"},

            {"items_list_table_header_name", "Item name"},

            {"item_creation_pane_title", "Items creation window"},
            {"item_creation_pane_back_button", "Back"},
            {"item_creation_pane_add_button", "Add"},
            {"item_creation_pane_create_button", "Create"},
            {"item_creation_pane_update_button", "Update"},

            {"piece_creation_pane_name_label", "Item name"},
            {"piece_creation_pane_weight_label", "Weight"},

            {"rock_creation_pane_name_label", "Item name"},
            {"rock_creation_pane_stone_weight_label", "Stone weight"},
            {"rock_creation_pane_ore_weight_label", "Ore weight"},

            {"mining_instrument_creation_pane_name_label", "Item name"},
            {"mining_instrument_creation_pane_coefficient_label", "Coefficient"},

            {"hammer_creation_pane_name_label", "Item name"},
            {"hammer_creation_pane_head_material_label", "Head material"},
            {"hammer_creation_pane_handle_material_label", "Handle material"},

            {"update_location_pane_title", "Object update window"},
            {"update_location_pane_creation_hint", "Rewrite location params and click next"},

            {"locations_list_table_title", "Existing objects"},
            {"locations_list_table_header_name", "Location name"},
            {"locations_list_table_header_area", "Area"},
            {"locations_list_table_header_position", "Position"},
            {"locations_list_table_header_date", "Creation date"},
            {"locations_list_table_header_items", "Items"},
            {"locations_list_table_header_owner", "Owner"},

            {"items_list_table_title", "Items of object"},
            {"items_list_table_header_type", "Type"},
            {"items_list_table_header_params", "Parameters"},

            {"login_form_model_invalid_login", "Invalid login!"},
            {"login_form_model_invalid_password", "Invalid password!"},

            {"registration_form_model_invalid_login", "Invalid login!"},
            {"registration_form_model_invalid_email", "Invalid email!"},
            {"registration_form_model_starts_authorization", "Registration in process"},

            {"views_menu_table", "Table"},
            {"views_menu_creation", "Creation"},
            {"views_menu_visualisation", "Visualisation"},
            {"views_menu_menu", "Navigation menu"},

            {"location_creation_model_swapped_x", "X coordinate of bottom-left point is bigger than X coordinate of top-right point!"},
            {"location_creation_model_equals_x", "X coordinate of bottom-left and X coordinate of top-right point are equal!"},
            {"location_creation_model_swapped_y", "Y coordinate of bottom-left point is bigger than Y coordinate of top-right point!"},
            {"location_creation_model_equals_y", "Y coordinate of bottom-left and Y coordinate of top-right point are equal!"},
            {"location_creation_model_empty_name", "Name field is empty!"},

            {"items_menu_piece", "Piece"},
            {"items_menu_rock", "Rock"},
            {"items_menu_mining_instrument", "Mining instrument"},
            {"items_menu_hammer", "Hammer"},

            {"piece_invalid_weight", "Weight of piece isn't a float!"},
            {"piece_negative_weight", "Weight of piece can't be negative!"},
            {"rock_invalid_stone_weight", "Stone weight of rock isn't a float!"},
            {"rock_negative_stone_weight", "Weight of stone can't be negative!"},
            {"rock_invalid_ore_weight", "Ore weight of rock isn't a float!"},
            {"rock_negative_ore_weight", "Weight of ore can't be negative!"},
            {"mining_instrument_invalid_coefficient", "Coefficient of mining instrument isn't a float!"},
            {"mining_instrument_coefficient_out_of_diapason", "Coefficient of mining instrument is out of diapason 0 and 1!"},
            {"hammer_invalid_head_material", "Select head material!"},
            {"hammer_invalid_handle_material", "Select handle material!"},

            {"object_item_rock_stone_weight", "Stone weight"},
            {"object_item_rock_ore_weight", "Ore weight"},
            {"object_item_piece_weight", "Weight"},
            {"object_item_mininginstrument_coefficient", "Coefficient"},
            {"object_item_hammer_head_name", "Head material"},
            {"object_item_hammer_handle_name", "Handle material"},

            {"logout_message", "You left from system"},
            {"logout_not_authorized", "It seems like someone is using this account"},
            {"logout_no_response", "It seems like the server is disable now. Try again later"},
            {"location_creation_successful_added", "Location successful added"},

            {"window_title_login_page", "Objects service application (login page)"},
            {"window_title_registration_page", "Objects service application (registration page)"},
            {"window_title_location_creation_page", "Objects service application (location creation page)"},
            {"window_title_location_update_page", "Objects service application (locations update page)"},
            {"window_title_locations_table_page", "Objects service application (locations table page)"},
            {"window_title_visualisation_page", "Objects service application (visualisation page)"},
            {"window_title_settings_page", "Objects service application (settings page)"},
            {"window_title_items_adding_page", "Objects service application (items adding page)"},
            {"window_title_items_table_page", "Objects service application (items table page)"},

            {"location_filer_pane_field_list_label", "Field"},
            {"location_filer_pane_field_list_name", "Location name"},
            {"location_filer_pane_field_list_area", "Area"},
            {"location_filer_pane_field_list_position", "Position"},
            {"location_filer_pane_field_list_items", "Items"},
            {"location_filer_pane_field_list_creation_date", "Creation date"},
            {"location_filer_pane_field_list_owner", "Owner"},

            {"items_filter_pane_field_list_title", "Field"},
            {"items_filter_pane_field_list_type", "Type"},
            {"items_filter_pane_field_list_name", "Name"},

            {"head_material_combo_box_wood", "Wood"},
            {"head_material_combo_box_iron", "Iron"},
            {"head_material_combo_box_steel", "Steel"},
            {"head_material_combo_box_plastic", "Plastic"},
            {"head_material_combo_box_stone", "Stone"},

            {"items_table_type_cell_piece", "Piece"},
            {"items_table_type_cell_rock", "Rock"},
            {"items_table_type_cell_mininginstrument", "Mining instrument"},
            {"items_table_type_cell_hammer", "Hammer"},

            {"location_table_empty_placeholder", "There are no locations"},
            {"items_table_empty_placeholder", "There are no items"},
            {"items_list_empty_placeholder", "There are no items"},

            {"context_menu_show", "Show"},
            {"context_menu_edit", "Edit"},
            {"context_menu_delete", "Delete"},

            {"creation_pane_image_chooser", "Choose image"},
            {"icon_invalid_format", "Unsupported format of image!"}

    };

}
