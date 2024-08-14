package net.detrovv.themod.souls;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ListSoulSupplier implements Supplier<List<Soul>>
{
    @Override
    public List<Soul> get() {
        return new ArrayList<>();
    }
}
