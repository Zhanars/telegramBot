import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ReplyButtons {
    //knopka v klave
    public static synchronized void firstButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        KeyboardRow keyboardThirdRow = new KeyboardRow();
        KeyboardRow keyboardfourthRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":school:ИПК Универ")));
        keyboardFirstRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":clock830:Контроль доступа")));
        keyboardFirstRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":bar_chart:Статистика")));
        keyboardSecondRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":clipboard:Опросы")));
        keyboardSecondRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":newspaper:Для абитуриентов")));
        keyboardThirdRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":man_student: :woman_student:Список вакансий")));
        keyboardfourthRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":email:Обратная связь")));
        keyboardfourthRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":telephone_receiver:Контакты")));
        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        keyboardRowList.add(keyboardThirdRow);
        keyboardRowList.add(keyboardfourthRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }
    public static synchronized void ContactButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":telephone_receiver:Отправить номер телефона")).setRequestContact(true));
        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    public static synchronized void UniverButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        KeyboardRow keyboardThirdRow = new KeyboardRow();
        KeyboardRow keyboardFourthRow = new KeyboardRow();
        KeyboardRow keyboardFifthRow = new KeyboardRow();
        KeyboardRow keyboardSixthRow = new KeyboardRow();
        if (telegrambotsql.getStatus(sendMessage.getChatId()) == 1){
            keyboardFirstRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":100:Текущие оценки")));
            keyboardFirstRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":bookmark:Транскрипт")));
            keyboardSecondRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":moneybag:Задолжность по оплате")));
            keyboardThirdRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":world_map:Расписание занятий")));
            keyboardThirdRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":world_map:Расписание экзаменов")));
            keyboardFourthRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":file_folder:УМКД")));
            keyboardFourthRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":spiral_calendar_pad:Календарь")));
            keyboardFifthRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":telephone_receiver:Контакты эдвайзера")));
            keyboardFifthRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":key:")+"Сброс пароля"));
            keyboardSixthRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":rewind:Вернуться на главную")));
        }else{

            keyboardFirstRow.add(new KeyboardButton("/bla person"));
            keyboardFirstRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":rewind:Вернуться на главную")));
        }
        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        keyboardRowList.add(keyboardThirdRow);
        keyboardRowList.add(keyboardFourthRow);
        keyboardRowList.add(keyboardFifthRow);
        keyboardRowList.add(keyboardSixthRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }
    public static synchronized void SKUDButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":date:Выписка на месяц")));
        keyboardFirstRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":date:Выписка на прошлый месяц")));
        keyboardSecondRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":rewind:Вернуться на главную")));
        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }
    public static synchronized void CodeButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        if (telegrambotsql.getStatus(sendMessage.getChatId()) == 1){
            keyboardFirstRow.add(new KeyboardButton("fd"));
            keyboardFirstRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":rewind:Вернуться на главную")));
        }else{
            keyboardFirstRow.add(new KeyboardButton("/bla person"));
            keyboardFirstRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":rewind:Вернуться на главную")));
        }
        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    public static synchronized void zeroButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":newspaper:Для абитуриентов")));
        keyboardFirstRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":telephone_receiver:Контакты")));
        keyboardSecondRow.add(new KeyboardButton(EmojiParser.parseToUnicode("Отчет по свободным местам в общежитии")));
        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }
    public static synchronized void abiturientButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        KeyboardRow keyboardThirdRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("📰Буклет"));
        keyboardFirstRow.add(new KeyboardButton("\uD83C\uDFACВидео"));
        keyboardSecondRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":rewind:Вернуться на главную")));
        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        keyboardRowList.add(keyboardThirdRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }
    public static synchronized void ratingButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        KeyboardRow keyboardThirdRow = new KeyboardRow();
        KeyboardRow keyboardFourthRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("По курсу"));
        keyboardSecondRow.add(new KeyboardButton("По факультету"));
        keyboardThirdRow.add(new KeyboardButton("По специальности"));
        keyboardFourthRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":rewind:Вернуться на главную")));
        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        keyboardRowList.add(keyboardThirdRow);
        keyboardRowList.add(keyboardFourthRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }
    public static synchronized void staticButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        KeyboardRow keyboardThirdRow = new KeyboardRow();
        KeyboardRow keyboardFourthRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Отчет по общежитию"));
        keyboardSecondRow.add(new KeyboardButton("Рейтинг по GPA"));
        keyboardFourthRow.add(new KeyboardButton(EmojiParser.parseToUnicode(":rewind:Вернуться на главную")));
        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        keyboardRowList.add(keyboardFourthRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }
}
