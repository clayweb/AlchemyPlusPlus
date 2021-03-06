package alchemyplusplus.proxy;

import alchemyplusplus.tileentities.diffuser.TileEntityDiffuser;
import alchemyplusplus.tileentities.distillery.TileEntityDistillery;
import alchemyplusplus.tileentities.mixer.TileEntityLiquidMixer;
import alchemyplusplus.tileentities.potionjug.TileEntityPotionJug;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class CommonProxy
{

    public static int RENDER_ID;
    public static String displayName;

    public void registerRenderers()
    {

    }

    public void registerTileEntitys()
    {
        // @TODO - add tileEntity names to Resources reference class per https://github.com/jakimfett/AlchemyPlusPlus/issues/6
        GameRegistry.registerTileEntity(TileEntityDiffuser.class, "diffuserTE");
        GameRegistry.registerTileEntity(TileEntityDistillery.class, "distilleryTE");
        GameRegistry.registerTileEntity(TileEntityLiquidMixer.class, "liquidMixerTE");
        GameRegistry.registerTileEntity(TileEntityPotionJug.class, "potionJugTE");
    }

    public World getClientWorld()
    {
        return null;
    }

    public void registerHooks()
    {
    }

    public EntityPlayer findEntityPlayerByName(String name)
    {

        EntityPlayer player;
        player = MinecraftServer.getServer().getConfigurationManager().func_152612_a(name);

        if (player != null)
        {
            return player;
        }

        return null;
    }

    public String getCurrentLanguage()
    {
        return null;
    }

    public void addName(Object obj, String s)
    {
    }

    public void addLocalization(String s1, String string)
    {
    }

    public String getItemDisplayName(ItemStack newStack)
    {
        return this.displayName;
    }
}
