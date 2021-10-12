import java.util.Collection;

public interface Tuple
{
    public Collection<Object> getAllFields();
    public Object getField(Integer field);
}
