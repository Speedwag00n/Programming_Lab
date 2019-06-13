package resources.lang;

import java.util.ListResourceBundle;

public class lang_et_EE extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return contents;
    }

    private Object[][] contents = {

            {"header_login_button", "Sisselogimine"},
            {"header_registration_button", "Registreerimine"},
            {"header_exit_button", "Välju"},
            {"login_pane_title", "Autoriseerimise aken"},
            {"login_pane_login_label", "Sisselogimine"},
            {"login_pane_password_label", "Parool"},
            {"login_pane_login_button", "Logi sisse"},
            {"login_pane_login_hint", "Kirjutage kasutajanimi ja parool. Vajutage \"Logi sisse\""},

            {"registration_pane_title", "Registreerimisaken"},
            {"registration_pane_login_label", "Sisselogimine"},
            {"registration_pane_email_label", "E-post"},
            {"registration_pane_register_button", "Laulge"},
            {"registration_pane_register_hint", "Kirjutage sisselogimine ja e-post. Pärast vajutamist \"Laulge\""},
            {"registration_pane_login_hint", "Sisselogimine peab olema lühem kui 30 sümbolit ja sisaldama ainult a-z, A-Z ja 0-9 sümboleid"},

            {"settings_pane_title", "Seadete aken"},
            {"settings_lang_combo_box", "Keel"},
            {"settings_pane_port_label", "Serveri port"},
            {"settings_pane_apply_button", "Rakenda"},
            {"settings_applied", "Rakendused"},
            {"settings_negative_port", "Port ei saa olla negatiivne!"},
            {"settings_to_big_port", "Port peab olema väiksem kui 65535 või võrdne sellega!"},

            {"header_logout_button", "Väljumist"},
            {"header_greetings_label", "Tere tulemast,"},

            {"location_creation_pane_title", "Objekti loomise aken"},
            {"location_creation_pane_name_label", "Asukoha nimi"},
            {"location_creation_pane_bottom_left_point_label", "Vasakpoolne punkt"},
            {"location_creation_pane_top_right_label", "Üleval paremal"},
            {"location_creation_x_label", "X"},
            {"location_creation_y_label", "Y"},
            {"location_creation_pane_next_button", "Edasi"},
            {"location_creation_pane_creation_hint", "Pange väärtused kõikidesse väljadesse ja vajutage \"Next\""},

            {"items_list_pane_title", "Lisatud üksused"},

            {"items_list_table_header_name", "Üksuse nimi"},

            {"item_creation_pane_title", "Üksuste loomise aken"},
            {"item_creation_pane_back_button", "Tagasi"},
            {"item_creation_pane_add_button", "Lisa"},
            {"item_creation_pane_create_button", "Loo"},
            {"item_creation_pane_update_button", "Update"},

            {"piece_creation_pane_name_label", "Üksuse nimi"},
            {"piece_creation_pane_weight_label", "Kaal"},

            {"rock_creation_pane_name_label", "üksuse nimi"},
            {"rock_creation_pane_stone_weight_label", "Kivi kaal"},
            {"rock_creation_pane_ore_weight_label", "Ore kaal"},

            {"mining_instrument_creation_pane_name_label", "Üksuse nimi"},
            {"mining_instrument_creation_pane_coefficient_label", "Koefitsient"},

            {"hammer_creation_pane_name_label", "Objekti nimi"},
            {"hammer_creation_pane_head_material_label", "Pea materjal"},
            {"hammer_creation_pane_handle_material_label", "Käsitle materjali"},

            {"update_location_pane_title", "Objekti uuendamise aken"},
            {"update_location_pane_creation_hint", "Asukoha parameetrite ümberkirjutamine ja klõpsake nuppu Järgmine"},

            {"places_list_table_title", "Olemasolevad objektid"},
            {"places_list_table_header_name", "Asukoha nimi"},
            {"places_list_table_header_area", "Piirkond"},
            {"places_list_table_header_position", "Asukoht"},
            {"places_list_table_header_date", "loomise kuupäev"},
            {"places_list_table_header_items", "Kirjed"},
            {"places_list_table_header_owner", "Omanik"},

            {"items_list_table_title", "Objekti objektid"},
            {"items_list_table_header_type", "Tüüp"},
            {"items_list_table_header_params", "Parameetrid"},

            {"login_form_model_invalid_login", "Kehtetu sisselogimine!"},
            {"login_form_model_invalid_password", "Kehtetu parool!"},

            {"registration_form_model_invalid_login", "Kehtetu sisselogimine!"},
            {"registration_form_model_invalid_email", "Kehtetu e-post!"},
            {"registration_form_model_starts_authorization", "Registreerimine protsessis"},

            {"views_menu_table", "Tabel"},
            {"views_menu_creation", "Loomine"},
            {"views_menu_visualisation", "Visualiseerimine"},
            {"views_menu_menu", "Navigatsioonimenüü"},

            {"location_creation_model_swapped_x", "Alumise vasaku punkti X koordinaat on suurem kui ülemise parempoolse punkti X koordinaat!"},
            {"location_creation_model_equals_x", "Ülemise parempoolse punkti X vasaku ja X koordinaadi koordinaadid on võrdsed!"},
            {"location_creation_model_swapped_y", "Alumise vasaku punkti Y-koordinaat on suurem kui ülemise parempoolse punkti Y-koordinaat!"},
            {"location_creation_model_equals_y", "Ülemise parempoolse punkti Y vasaku ja Y-koordinaadi Y-koordinaat on võrdsed!"},
            {"location_creation_model_empty_name", "Nimi väli on tühi!"},

            {"items_menu_piece", "Tükk"},
            {"items_menu_rock", "Kivi"},
            {"items_menu_mining_instrument", "Kaevandusvahend"},
            {"items_menu_hammer", "Haammer"},

            {"piece_invalid_weight", "Tükkide kaal ei ole ujuv!"},
            {"piece_negative_weight", "Tükkide kaal ei saa olla negatiivne!"},
            {"rock_invalid_stone_weight", "Kivimass ei ole ujuk!"},
            {"rock_negative_stone_weight", "Kivi kaal ei saa olla negatiivne!"},
            {"rock_invalid_ore_weight", "Kalju kaalu kaal ei ole ujuv!"},
            {"rock_negative_ore_weight", "Maagi kaal ei saa olla negatiivne!"},
            {"mining_instrument_invalid_coefficient", "Kaevandusvahendi koefitsient ei ole ujuk!"},
            {"mining_instrument_coefficient_out_of_diapason", "Kaevandamise instrumendi koefitsient on väljapoole diapasooni 0 ja 1!"},
            {"hammer_invalid_head_material", "Vali peamaterjal!"},
            {"hammer_invalid_handle_material", "Vali käepide materjal!"},

            {"object_item_rock_stone_weight", "Kivi kaal"},
            {"object_item_rock_ore_weight", "Ore kaal"},
            {"object_item_piece_weight", "Kaal"},
            {"object_item_mininginstrument_coefficient", "Koefitsient"},
            {"object_item_hammer_head_name", "Pea materjali"},
            {"object_item_hammer_handle_name", "Käsitle materjali"},

            {"logout_message", "Sa lahkusid süsteemist"},
            {"logout_not_authorized", "Tundub, et keegi seda kontot kasutab"},
            {"logout_no_response", "Tundub, et server on nüüd keelatud. Proovige hiljem uuesti"},
            {"location_creation_successful_added", "Asukoht edukalt lisatud"},

            {"window_title_login_page", "Objektide teenuse rakendus (sisselogimise leht)"},
            {"window_title_registration_page", "Objektide teenuse rakendus (registreerimise leht)"},
            {"window_title_location_creation_page", "Objektide teenuse rakendus (asukoha loomise leht)"},
            {"window_title_location_update_page", "Objektide teenuse rakendus (asukoha värskendamise leht)"},
            {"window_title_locations_table_page", "Objektide teenuse rakendus (asukohtade tabeli leht)"},
            {"window_title_visualisation_page", "Objektide teenuse rakendus (visualiseerimise leht)"},
            {"window_title_settings_page", "Objektide teenuse rakendus (seadete leht)"},
            {"window_title_items_adding_page", "Objektide teenuse rakendus (üksuste lisamise leht)"},
            {"window_title_items_table_page", "Objektide teenuse rakendus (üksuste tabeli leht)"},

            {"location_filer_pane_field_list_label", "Väli"},
            {"location_filer_pane_field_list_name", "Asukoha nimi"},
            {"location_filer_pane_field_list_area", "Piirkond"},
            {"location_filer_pane_field_list_position", "Asukoht"},
            {"location_filer_pane_field_list_items", "Kirjed"},
            {"location_filer_pane_field_list_creation_date", "Loomise kuupäev"},
            {"location_filer_pane_field_list_owner", "Omanik"},

            {"items_filter_pane_field_list_title", "Väli"},
            {"items_filter_pane_field_list_type", "Tüüp"},
            {"items_filter_pane_field_list_name", "Nimi"},

            {"head_material_combo_box_wood", "Puit"},
            {"head_material_combo_box_iron", "Raud"},
            {"head_material_combo_box_steel", "Teras"},
            {"head_material_combo_box_plastic", "Plastist"},
            {"head_material_combo_box_stone", "Kivi"},

            {"items_table_type_cell_piece", "Tükk"},
            {"items_table_type_cell_rock", "Kivi"},
            {"items_table_type_cell_mininginstrument", "Kaevandamisinstrument"},
            {"items_table_type_cell_hammer", "Haammer"},

            {"location_table_empty_placeholder", "Asukohad puuduvad"},
            {"items_table_empty_placeholder", "Üksusi pole"},
            {"items_list_empty_placeholder", "Üksusi pole"},

            {"context_menu_show", "Näita"},
            {"context_menu_edit", "Muuda"},
            {"context_menu_delete", "Kustuta"},

            {"creation_pane_image_chooser", "Valige pilt"},
            {"icon_invalid_format", "Pildi toetamata vorming!"}

    };

}
