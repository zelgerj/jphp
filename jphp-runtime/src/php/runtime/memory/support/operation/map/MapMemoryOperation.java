package php.runtime.memory.support.operation.map;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.memory.support.operation.GenericMemoryOperation;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapMemoryOperation extends GenericMemoryOperation<Map> {
    public MapMemoryOperation(Type... genericTypes) {
        super(genericTypes);

        if (genericTypes == null) {
            operations = new MemoryOperation[]{
                    MemoryOperation.get(Memory.class, null),
                    MemoryOperation.get(Memory.class, null)
            };
        }
    }

    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[]{Map.class};
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map convert(Environment env, TraceInfo trace, Memory arg) {
        Map result = this.createHashMap();

        ForeachIterator iterator = arg.getNewIterator(env);

        while (iterator.next()) {
            result.put(
                    operations[0].convert(env, trace, iterator.getMemoryKey()),
                    operations[1].convert(env, trace, iterator.getValue())
            );
        }

        return result;
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Map arg) {
        ArrayMemory result = new ArrayMemory();

        for (Object _entry : arg.entrySet()) {
            Map.Entry entry = (Map.Entry) _entry;

            result.refOfIndex(operations[0].unconvert(env, trace, entry.getKey()))
                    .assign(operations[1].unconvert(env, trace, entry.getValue()));
        }

        return null;
    }

    protected Map createHashMap() {
        return new LinkedHashMap();
    }
}
