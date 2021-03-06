package alchemyplusplus.tileentities.potionjug;

import alchemyplusplus.block.BlockRegistry;
import alchemyplusplus.gui.CreativeTab;
import alchemyplusplus.network.PacketDispatcher;
import alchemyplusplus.reference.Textures;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockPotionJug extends BlockContainer
{
    public BlockPotionJug(String blockname)
    {
        super(Material.glass);
        this.setStepSound(Block.soundTypeGlass);
        this.setBlockName(blockname);
        this.setCreativeTab(CreativeTab.APP_TAB);
        this.setBlockTextureName(Textures.Icon.POTION_JUG);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntityPotionJug();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        ItemStack stack = new ItemStack(BlockRegistry.potionJug, 1, 0);
        Random random = new Random();

        if (world.getTileEntity(x, y, z) != null)
        {
            int potionID = (((TileEntityPotionJug) world.getTileEntity(x, y, z)).potionID);
            int has = (((TileEntityPotionJug) world.getTileEntity(x, y, z)).containerHas);
            if (potionID != 0 && has != 0)
            {
                stack.stackTagCompound = new NBTTagCompound();
                stack.getTagCompound().setShort("effectID", (short) potionID);
                stack.getTagCompound().setShort("containerHas", (short) has);
            }
        }

        float f = random.nextFloat() * 0.8F + 0.1F;
        float f1 = random.nextFloat() * 0.8F + 0.1F;
        float f2 = random.nextFloat() * 0.8F + 0.1F;

        EntityItem entityitem = new EntityItem(world, (double) ((float) x + f), (double) ((float) y + f1), (double) ((float) z + f2),
                stack);

        entityitem.motionX = (double) ((float) random.nextGaussian() * 0.05F);
        entityitem.motionY = (double) ((float) random.nextGaussian() * 0.05F + 0.2F);
        entityitem.motionZ = (double) ((float) random.nextGaussian() * 0.05F);

        world.spawnEntityInWorld(entityitem);

        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int par5)
    {
        TileEntityPotionJug te = (TileEntityPotionJug) world.getTileEntity(x, y, z);
        return (int) Math.floor(te.containerHas * 16f / te.containerMax);
    }

    @Override
    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int a, float b, float c, float g)
    {
        world.notifyBlockChange(x, y, z, this);

        if (player.isSneaking())
        {

            if (((TileEntityPotionJug) world.getTileEntity(x, y, z)).containerHas > 0)
            {
                ((TileEntityPotionJug) world.getTileEntity(x, y, z)).containerHas--;

                List effectList = PotionHelper.getPotionEffects(((TileEntityPotionJug) world.getTileEntity(x, y, z)).potionID, true);
                for (int i = 0; i < effectList.size(); i++)
                {
                    player.addPotionEffect((PotionEffect) effectList.get(i));
                }
                if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
                {
                    PacketDispatcher.sendPacketToAllPlayers(world.getTileEntity(x, y, z).getDescriptionPacket());
                }

            }
        }

        ItemStack stack = player.getCurrentEquippedItem();
        TileEntityPotionJug te = (TileEntityPotionJug) world.getTileEntity(x, y, z);
        if (stack == null)
        {
            return true;
        }
        if (stack.getItem() == Items.glass_bottle && stack.getItemDamage() == 0)
        {
            if (te.containerHas > 0)
            {
                stack.stackSize--;
                te.containerHas--;
                ItemStack potion = new ItemStack(Items.potionitem, 1, te.potionID);
                player.inventory.addItemStackToInventory(potion);
            }
        } else if (stack.getItem() == Items.potionitem && stack.getItemDamage() > 0 && !stack.hasTagCompound())
        {		//Custom potions are not allowed!
            if (te.containerHas == 0 || (stack.getItemDamage() == te.potionID && (te.containerHas < te.containerMax)))
            {
                te.containerHas++;
                te.potionID = stack.getItemDamage();
                stack.stackSize--;
                ItemStack potion = new ItemStack(Items.glass_bottle, 1, 0);
                player.inventory.addItemStackToInventory(potion);
            }
        }
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        {
            PacketDispatcher.sendPacketToAllPlayers(world.getTileEntity(x, y, z).getDescriptionPacket());
        }
        return true;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
}