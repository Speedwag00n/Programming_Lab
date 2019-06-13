package resources.lang;

import java.util.ListResourceBundle;

public class lang_ru_RU extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return contents;
    }

    private Object[][] contents = {

            {"header_login_button", "Логин"},
            {"header_registration_button", "Регистрация"},
            {"header_exit_button", "Выход"},
            {"login_pane_title", "Окно авторизации"},
            {"login_pane_login_label", "Войти"},
            {"login_pane_password_label", "Пароль"},
            {"login_pane_login_button", "Войти"},
            {"login_pane_login_hint", "Введите логин и пароль. После нажажмите кнопку \"Войти\""},

            {"registration_pane_title", "Окно регистрации"},
            {"registration_pane_login_label", "Логин"},
            {"registration_pane_email_label", "Почта"},
            {"registration_pane_register_button", "Регистрация"},
            {"registration_pane_register_hint", "Введите логин и адрес электронной почты. После нажмите кнопку \"Регистрация\""},
            {"registration_pane_login_hint", "Логин должен быть короче 30 символов и содержать только символы a-z, A-Z и 0-9"},

            {"settings_pane_title", "Окно настроек"},
            {"settings_lang_combo_box", "Язык"},
            {"settings_pane_port_label", "Порт сервера"},
            {"settings_pane_apply_button", "Применить"},
            {"settings_applied", "Настройки применены"},
            {"settings_negative_port", "Порт не может быть отрицательным!"},
            {"settings_to_big_port", "Порт должен быть меньше 65535 или равен этому числу!"},

            {"header_logout_button", "Выход"},
            {"header_greetings_label", "Добро пожаловать,"},

            {"location_creation_pane_title", "Окно создания объекта"},
            {"location_creation_pane_name_label", "Название локации"},
            {"location_creation_pane_bottom_left_point_label", "Нижняя левая точка"},
            {"location_creation_pane_top_right_label", "Правая верхняя точка"},
            {"location_creation_x_label", "X"},
            {"location_creation_y_label", "Y"},
            {"location_creation_pane_next_button", "Далее"},
            {"location_creation_pane_creation_hint", "Поместите значения во все поля и нажмите \"Далее\""},

            {"items_list_pane_title", "Добавленные предметы"},

            {"items_list_table_header_name", "Имя предмета"},

            {"item_creation_pane_title", "Окно создания предметов"},
            {"item_creation_pane_back_button", "Назад"},
            {"item_creation_pane_add_button", "Добавить"},
            {"item_creation_pane_create_button", "Создать"},
            {"item_creation_pane_update_button", "Обновить"},

            {"piece_creation_pane_name_label", "Название предмета"},
            {"piece_creation_pane_weight_label", "Вес"},

            {"rock_creation_pane_name_label", "Название предмета"},
            {"rock_creation_pane_stone_weight_label", "Вес камня"},
            {"rock_creation_pane_ore_weight_label", "Вес руды"},

            {"mining_instrument_creation_pane_name_label", "Название предмета"},
            {"mining_instrument_creation_pane_coefficient_label", "Коэффициент"},

            {"hammer_creation_pane_name_label", "Название предмета"},
            {"hammer_creation_pane_head_material_label", "Материал бойка"},
            {"hammer_creation_pane_handle_material_label", "Материал ручки"},

            {"update_location_pane_title", "Окно обновления объекта"},
            {"update_location_pane_creation_hint", "Заполните поля и нажмите кнопку \"Далее\""},

            {"locations_list_table_title", "Существующие объекты"},
            {"locations_list_table_header_name", "Название локации"},
            {"locations_list_table_header_area", "Площадь"},
            {"locations_list_table_header_position", "Позиция"},
            {"locations_list_table_header_date", "Дата создания"},
            {"locations_list_table_header_items", "Предметы"},
            {"locations_list_table_header_owner", "Владелец"},

            {"items_list_table_title", "Предметы объекта"},
            {"items_list_table_header_type", "Тип"},
            {"items_list_table_header_params", "Параметры"},

            {"login_form_model_invalid_login", "Неверный логин!"},
            {"login_form_model_invalid_password", "Неверный пароль!"},

            {"registration_form_model_invalid_login", "Неверный логин!"},
            {"registration_form_model_invalid_email", "Неверный адрес электронной почты!"},
            {"registration_form_model_starts_authorization", "Регистрация в процессе"},

            {"views_menu_table", "Таблица"},
            {"views_menu_creation", "Создание"},
            {"views_menu_visualisation", "Визуализация"},
            {"views_menu_menu", "Меню навигации"},

            {"location_creation_model_swapped_x", "X координата нижней левой точки больше, чем X координаты верхней правой точки!"},
            {"location_creation_model_equals_x", "X координата левого нижнего угла и X координата правого верхнего угла равны!"},
            {"location_creation_model_swapped_y", "Y координата нижней левой точки больше, чем Y координата верхней правой точки!"},
            {"location_creation_model_equals_y", "Y координата левого нижнего угла и Y координата правого верхнего угла равны!"},
            {"location_creation_model_empty_name", "Поле названия пусто!"},

            {"items_menu_piece", "Кусок"},
            {"items_menu_rock", "Скала"},
            {"items_menu_mining_instrument", "Горнодобывающий инструмент"},
            {"items_menu_hammer", "Молоток"},

            {"piece_invalid_weight", "Вес куска не является рациональным числом!"},
            {"piece_negative_weight", "Вес куска не может быть отрицательным!"},
            {"rock_invalid_stone_weight", "Вес камня не является рациональным числом!"},
            {"rock_negative_stone_weight", "Вес камня не может быть отрицательным!"},
            {"rock_invalid_ore_weight", "Вес руды не является рациональным числом!"},
            {"rock_negative_ore_weight", "Вес руды не может быть отрицательным!"},
            {"mining_instrument_invalid_coefficient", "Коэффициент горнодобывающего инструмента не является рациональным числом!"},
            {"mining_instrument_coefficient_out_of_diapason", "Коэффициент горнодобывающего инструмента находится вне диапазона между 0 и 1!"},
            {"hammer_invalid_head_material", "Выберите материал бойка!"},
            {"hammer_invalid_handle_material", "Выберите материал ручки!"},

            {"object_item_rock_stone_weight", "Вес камня"},
            {"object_item_rock_ore_weight", "Вес руды"},
            {"object_item_piece_weight", "Вес"},
            {"object_item_mininginstrument_coefficient", "Коэффициент"},
            {"object_item_hammer_head_name", "Материал бойка"},
            {"object_item_hammer_handle_name", "Материал ручки"},

            {"logout_message", "Вы вышли из системы"},
            {"logout_not_authorized", "Похоже, что кто-то уже использует этот аккаунт"},
            {"logout_no_response", "Сервер недоступен в данный момент. Повторите запрос позже"},
            {"location_creation_successful_added", "Локация успешно добавлена"},

            {"window_title_login_page", "Сервис объектов (страница входа)"},
            {"window_title_registration_page", "Сервис объектов (страница регистрации)"},
            {"window_title_location_creation_page", "Сервис объектов (страница создания локаций)"},
            {"window_title_location_update_page", "Сервис объектов (страница обновления локаций)"},
            {"window_title_locations_table_page", "Сервис объектов (страница таблицы локаций)"},
            {"window_title_visualisation_page", "Сервис объектов (страница визуализации)"},
            {"window_title_settings_page", "Сервис объектов (страница настроек)"},
            {"window_title_items_adding_page", "Сервис объектов (страница добавления предметов)"},
            {"window_title_items_table_page", "Сервис объектов (страница таблицы предметов)"},

            {"location_filer_pane_field_list_label", "Поле"},
            {"location_filer_pane_field_list_name", "Название локации"},
            {"location_filer_pane_field_list_area", "Площадь"},
            {"location_filer_pane_field_list_position", "Позиция"},
            {"location_filer_pane_field_list_items", "Предметы"},
            {"location_filer_pane_field_list_creation_date", "Дата создания"},
            {"location_filer_pane_field_list_owner", "Владелец"},

            {"items_filter_pane_field_list_title", "Поле"},
            {"items_filter_pane_field_list_type", "Тип"},
            {"items_filter_pane_field_list_name", "Название"},

            {"head_material_combo_box_wood", "Дерево"},
            {"head_material_combo_box_iron", "Железо"},
            {"head_material_combo_box_steel", "Сталь"},
            {"head_material_combo_box_plastic", "Пластик"},
            {"head_material_combo_box_stone", "Камень"},

            {"items_table_type_cell_piece", "Кусок"},
            {"items_table_type_cell_rock", "Скала"},
            {"items_table_type_cell_mininginstrument", "Горнодобывающий инструмент"},
            {"items_table_type_cell_hammer", "Молоток"},

            {"location_table_empty_placeholder", "Локации отсутствуют"},
            {"items_table_empty_placeholder", "Предметы отсутствуют"},
            {"items_list_empty_placeholder", "Предметы отсутствуют"},

            {"context_menu_show", "Просмотр"},
            {"context_menu_edit", "Редактировать"},
            {"context_menu_delete", "Удалить"},

            {"creation_pane_image_chooser", "Выбрать изображение"},
            {"icon_invalid_format", "Неподдерживаемый формат изображения!"}
            
    };

}
