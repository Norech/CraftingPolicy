package com.norech.craftingpolicy.util;

import java.lang.reflect.Method;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import com.norech.craftingpolicy.util.ReflectionUtils.*;

/**
 * Create a "Virtual" book gui that doesn't require the user to have a book in their hand.
 * Requires ReflectionUtil class.
 * Built for Minecraft 1.9
 * @author Jed
 * @author Norech
 *
 */
public class BookUtil {
    private static boolean initialised = false;
    private static Method getHandle;
    private static Method openBook;

    static {
        try {
            /* org.bukkit.craftbukkit.$VERSION.entity.CraftPlayer.getHandle() */
            getHandle = ReflectionUtils.getMethod("CraftPlayer", PackageType.CRAFTBUKKIT_ENTITY, "getHandle");

            /* net.minecraft.server.$VERSION.EntityPlayer.a(net.minecraft.server.$VERSION.ItemStack, net.minecraft.server.$VERSION.EnumHand) */
            openBook = ReflectionUtils.getMethod("EntityPlayer", PackageType.MINECRAFT_SERVER, "a", PackageType.MINECRAFT_SERVER.getClass("ItemStack"), PackageType.MINECRAFT_SERVER.getClass("EnumHand"));

            initialised = true;
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            Bukkit.getServer().getLogger().warning("Cannot open virtual book!");
            initialised = false;
        }
    }
    public static boolean isInitialised(){
        return initialised;
    }
    /**
     * Open a "Virtual" Book ItemStack.
     * @param i Book ItemStack.
     * @param p Player that will open the book.
     * @return
     */
    public static boolean openBook(ItemStack i, Player p) {
        if (!initialised) return false;
        ItemStack held = p.getInventory().getItemInMainHand();
        try {
            p.getInventory().setItemInMainHand(i);
            sendPacket(i, p);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            initialised = false;
        }
        p.getInventory().setItemInMainHand(held);
        return initialised;
    }

    /**
     * Close the "Virtual" Book ItemStack.
     * @param p Player that will close the book.
     */
    public static void closeBook(Player p) {
        // Spigot can close current book by closing inventory.
        p.closeInventory();
    }

    private static void sendPacket(ItemStack i, Player p) throws ReflectiveOperationException {
        Object entityPlayer = getHandle.invoke(p);
        /* enumHand is a representation of net.minecraft.server.$VERSION.EnumHand() */
        Class<?> enumHand = PackageType.MINECRAFT_SERVER.getClass("EnumHand");
        Object[] enumArray = enumHand.getEnumConstants();
        openBook.invoke(entityPlayer, getItemStack(i), enumArray[0]);
    }

    public static Object getItemStack(ItemStack item) {
        try {
            /* org.bukkit.craftbukkit.$VERSION.inventory.CraftItemStack.asNMSCopy(ItemStack item) */
            Method asNMSCopy = ReflectionUtils.getMethod(PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftItemStack"), "asNMSCopy", ItemStack.class);

            return asNMSCopy.invoke(PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftItemStack"), item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Set the pages of the book in JSON format.
     * @param metadata BookMeta of the Book ItemStack.
     * @param pages Each page to be added to the book.
     */
    @SuppressWarnings("unchecked")
    public static void setPages(BookMeta metadata, List<String> pages) {
        List<Object> p;
        Object page;
        try {
            /* org.bukkit.craftbukkit.$VERSION.inventory.CraftMetaBook.pages */
            p = (List<Object>) ReflectionUtils.getField(PackageType.CRAFTBUKKIT_INVENTORY.getClass("CraftMetaBook"), true, "pages").get(metadata);  // BookMeta is implemented from CraftMetaBook

            for (String text : pages) {
                /* new net.minecraft.server.$VERSION.IChatBaseComponent.ChatSerializer() */
                Class<?> chatSerializer = ReflectionUtils.PackageType.MINECRAFT_SERVER.getClass("IChatBaseComponent$ChatSerializer");

                /* net.minecraft.server.$VERSION.IChatBaseComponent.ChatSerializer.a(String string) */
                page = ReflectionUtils.invokeMethod(chatSerializer.newInstance(), "a", text);

                p.add(page);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}