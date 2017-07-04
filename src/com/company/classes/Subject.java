package com.company.classes;

public enum Subject
{
    Астрономия,
    Биология,
    География,
    Иностранный_язык,
    Информатика,
    Искусство,
    История,
    Литература,
    Математика,
    Обществознание,
    Право,
    Психология,
    Русский_язык,
    Физика,
    Химия,
    Экология,
    Экономика,
    Остальные;

    public String toString()
    {
        String na = this.name();
        return na.replace('_', ' ');
    }
}
