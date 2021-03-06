package alchemyplusplus.tileentities.distillery;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerDistillery extends Container
{

    private final TileEntityDistillery distillery;
    private int distillingTicks = 0;
    private int fuel = 0;

    public ContainerDistillery(InventoryPlayer playerInv,
            TileEntityDistillery distillery)
    {
        this.distillery = distillery;

        this.addSlotToContainer(new SlotDistilleryByproduct(distillery, 0, 18,
                23)); // Byproduct
        this.addSlotToContainer(new SlotDistilleryInput(distillery, 1, 62, 23)); // input
        this.addSlotToContainer(new SlotDistilleryOutput(distillery, 2, 113, 58)); // output
        this.addSlotToContainer(new SlotDistilleryFuel(distillery, 3, 62, 58)); // fuel

        int i;

        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9,
                        8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));
        }

    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting)
    {
        super.addCraftingToCrafters(par1ICrafting);
        par1ICrafting.sendProgressBarUpdate(this, 0,
                this.distillery.distillingTicks);
        par1ICrafting.sendProgressBarUpdate(this, 1, this.distillery.fuel);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);

            if (this.distillingTicks != this.distillery.distillingTicks)
            {
                icrafting.sendProgressBarUpdate(this, 0,
                        this.distillery.distillingTicks);
            }

            if (this.fuel != this.distillery.fuel)
            {
                icrafting.sendProgressBarUpdate(this, 1, this.distillery.fuel);
            }
        }

        this.distillingTicks = this.distillery.distillingTicks;
        this.fuel = this.distillery.fuel;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot)
    {
        ItemStack stack = null;
        Slot slotObject = (Slot) inventorySlots.get(slot);

        // null checks and checks if the item can be stacked (maxStackSize > 1)
        if (slotObject != null && slotObject.getHasStack())
        {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();

            // merges the item into player inventory since its in the tileEntity
            if (slot < 9)
            {
                if (!this.mergeItemStack(stackInSlot, 0, 35, true))
                {
                    return null;
                }
            } // places it into the tileEntity is possible since its in the player
            // inventory
            else if (!this.mergeItemStack(stackInSlot, 1, 9, false))
            {
                return null;
            }

            if (stackInSlot.stackSize == 0)
            {
                slotObject.putStack(null);
            } else
            {
                slotObject.onSlotChanged();
            }

            if (stackInSlot.stackSize == stack.stackSize)
            {
                return null;
            }
            slotObject.onPickupFromSlot(player, stackInSlot);
        }
        return stack;
    }

    @Override
    public void updateProgressBar(int id, int value)
    {
        if (id == 0)
        {
            this.distillery.distillingTicks = value;
        } else if (id == 1)
        {
            this.distillery.fuel = value;
        }

    }

}
