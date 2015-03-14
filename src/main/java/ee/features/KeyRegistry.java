package ee.features;

import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;

public class KeyRegistry {
	public static final KeyBinding[] array = new KeyBinding[]{new KeyBinding("Crafting",Keyboard.KEY_C,"EELimited")};
	public static boolean isKeyDown(KeyBinding key)
	{
		return key.isPressed();
	}
	public static void registerKies()
	{
		for(int i = 0;i < array.length;i++)
		{
			ClientRegistry.registerKeyBinding(array[i]);
		}
	}
}
