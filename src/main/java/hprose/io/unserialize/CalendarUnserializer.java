/**********************************************************\
|                                                          |
|                          hprose                          |
|                                                          |
| Official WebSite: http://www.hprose.com/                 |
|                   http://www.hprose.org/                 |
|                                                          |
\**********************************************************/
/**********************************************************\
 *                                                        *
 * CalendarUnserializer.java                              *
 *                                                        *
 * Calendar unserializer class for Java.                  *
 *                                                        *
 * LastModified: Apr 17, 2016                             *
 * Author: Ma Bingyao <andot@hprose.com>                  *
 *                                                        *
\**********************************************************/

package hprose.io.unserialize;

import hprose.common.HproseException;
import static hprose.io.HproseTags.TagDate;
import static hprose.io.HproseTags.TagDouble;
import static hprose.io.HproseTags.TagEmpty;
import static hprose.io.HproseTags.TagInteger;
import static hprose.io.HproseTags.TagLong;
import static hprose.io.HproseTags.TagNull;
import static hprose.io.HproseTags.TagRef;
import static hprose.io.HproseTags.TagTime;
import hprose.util.DateTime;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.Calendar;

final class CalendarUnserializer implements Unserializer {

    public final static CalendarUnserializer instance = new CalendarUnserializer();

    private static Calendar toCalendar(Object obj) throws HproseException {
        if (obj instanceof DateTime) {
            return ((DateTime)obj).toCalendar();
        }
        throw ValueReader.castError(obj, Calendar.class);
    }

    private static Calendar toCalendar(int tag) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(tag - '0');
        return calendar;
    }

    final static Calendar read(Reader reader, ByteBuffer buffer) throws IOException {
        int tag = buffer.get();
        switch (tag) {
            case TagDate: return DefaultUnserializer.readDateTime(reader, buffer).toCalendar();
            case TagTime:  return DefaultUnserializer.readTime(reader, buffer).toCalendar();
            case TagNull:
            case TagEmpty: return null;
            case TagRef: return toCalendar(reader.readRef(buffer));
        }
        if (tag >= '0' && tag <= '9') return toCalendar(tag);
        switch (tag) {
            case TagInteger:
            case TagLong: {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(ValueReader.readLong(buffer));
                return calendar;
            }
            case TagDouble: {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Double.valueOf(ValueReader.readDouble(buffer)).longValue());
                return calendar;
            }
            default: throw ValueReader.castError(reader.tagToString(tag), Calendar.class);
        }
    }

    final static Calendar read(Reader reader, InputStream stream) throws IOException {
        int tag = stream.read();
        switch (tag) {
            case TagDate: return DefaultUnserializer.readDateTime(reader, stream).toCalendar();
            case TagTime:  return DefaultUnserializer.readTime(reader, stream).toCalendar();
            case TagNull:
            case TagEmpty: return null;
            case TagRef: return toCalendar(reader.readRef(stream));
        }
        if (tag >= '0' && tag <= '9') return toCalendar(tag);
        switch (tag) {
            case TagInteger:
            case TagLong: {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(ValueReader.readLong(stream));
                return calendar;
            }
            case TagDouble: {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Double.valueOf(ValueReader.readDouble(stream)).longValue());
                return calendar;
            }
            default: throw ValueReader.castError(reader.tagToString(tag), Calendar.class);
        }
    }

    public final Object read(Reader reader, ByteBuffer buffer, Class<?> cls, Type type) throws IOException {
        return read(reader, buffer);
    }

    public final Object read(Reader reader, InputStream stream, Class<?> cls, Type type) throws IOException {
        return read(reader, stream);
    }

}
