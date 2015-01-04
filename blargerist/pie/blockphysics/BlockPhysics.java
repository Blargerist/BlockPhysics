package blargerist.pie.blockphysics;

import java.util.Iterator;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraftforge.common.MinecraftForge;
import org.objectweb.asm.tree.*;
import blargerist.pie.blockphysics.events.BPEventHandler;
import blargerist.pie.blockphysics.network.proxies.ClientProxy;
import blargerist.pie.blockphysics.network.proxies.CommonProxy;
import blargerist.pie.blockphysics.util.BlockDef;
import blargerist.pie.blockphysics.util.EntityMovingBlock;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.EntityRegistry;

@Mod(modid = ModInfo.MODID, version = ModInfo.VERSION, dependencies = "")
public class BlockPhysics
{
	@SidedProxy(clientSide = "blargerist.pie.blockphysics.network.proxies.ClientProxy", serverSide = "blargerist.pie.blockphysics.network.proxies.CommonProxy")
	public static CommonProxy proxy;

	public static BlockDef[][] blockSet = new BlockDef[4096][16];
	public static boolean skipMove = false;
	
	@Instance("BlockPhysics")
	public static BlockPhysics instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		ModConfig.init(event.getSuggestedConfigurationFile());
		MinecraftForge.EVENT_BUS.register(new BPEventHandler());
		FMLCommonHandler.instance().bus().register(new BPEventHandler());
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		//proxy.initSounds();
		EntityRegistry.registerModEntity(EntityMovingBlock.class, "entityMovingBlock", 1, ModInfo.MODID, ModConfig.fallRenderRange, 3, true);
		proxy.initRenderers();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
	}

	public static void printInsns(ClassNode classNode)
	{
		ModInfo.Log.info("Printing methods in class: " + classNode.name);
		for (MethodNode method : classNode.methods)
		{
			ModInfo.Log.info(method.access + " - " + method.name + " - " + method.desc + " - " + method.signature);
		}
	}

	public static void printInsns(MethodNode methodNode)
	{
		ModInfo.Log.info("Printing instructions in method: " + methodNode.name);
		Iterator<AbstractInsnNode> iterator = methodNode.instructions.iterator();

		while (iterator.hasNext())
		{
			AbstractInsnNode node = iterator.next();

			if (node instanceof MethodInsnNode)
			{
				MethodInsnNode methodInsn = (MethodInsnNode) node;
				ModInfo.Log.info("MethodInsn- Owner: " + methodInsn.owner + " Name: " + methodInsn.name + " Desc: " + methodInsn.desc + " Itf: " + methodInsn.itf + " Opcode: " + methodInsn.getOpcode() + " Type: " + methodInsn.getType());
			}
			else if (node instanceof VarInsnNode)
			{
				VarInsnNode varNode = (VarInsnNode) node;
				ModInfo.Log.info("Var- Var:" + varNode.var + " Opcode: " + varNode.getOpcode() + " Type: " + varNode.getType());
			}
			else if (node instanceof FieldInsnNode)
			{
				FieldInsnNode fieldNode = (FieldInsnNode) node;
				ModInfo.Log.info("Field- Name:" + fieldNode.name + " Owner: " + fieldNode.owner + " Desc: " + fieldNode.desc + " Opcode: " + fieldNode.getOpcode() + " Type: " + fieldNode.getType());
			}
			else if (node instanceof FrameNode)
			{
				FrameNode frameNode = (FrameNode) node;
				ModInfo.Log.info("Frame- Type: " + frameNode.type + " Opcode: " + frameNode.getOpcode() + " Type: " + frameNode.getType());
			}
			else if (node instanceof IincInsnNode)
			{
				IincInsnNode iincNode = (IincInsnNode) node;
				ModInfo.Log.info("Iinc- Var:" + iincNode.var + " Incr: " + iincNode.incr + " Opcode: " + iincNode.getOpcode() + " Type: " + iincNode.getType());
			}
			else if (node instanceof InsnNode)
			{
				InsnNode insnNode = (InsnNode) node;
				ModInfo.Log.info("Insn- Opcode:" + insnNode.getOpcode() + " Type: " + insnNode.getType());
			}
			else if (node instanceof IntInsnNode)
			{
				IntInsnNode intInsnNode = (IntInsnNode) node;
				ModInfo.Log.info("IntInsn- Operand: " + intInsnNode.operand + " Opcode: " + intInsnNode.getOpcode() + " Type: " + intInsnNode.getType());
			}
			else if (node instanceof InvokeDynamicInsnNode)
			{
				InvokeDynamicInsnNode idiNode = (InvokeDynamicInsnNode) node;
				ModInfo.Log.info("InvokeDynamic- Name: " + idiNode.name + " Desc: " + idiNode.desc + " Opcode: " + idiNode.getOpcode() + " Type: " + idiNode.getType());
			}
			else if (node instanceof JumpInsnNode)
			{
				JumpInsnNode jumpNode = (JumpInsnNode) node;
				ModInfo.Log.info("Jump- Opcode: " + jumpNode.getOpcode() + " Type: " + jumpNode.getType());
			}
			else if (node instanceof LabelNode)
			{
				LabelNode labelNode = (LabelNode) node;
				ModInfo.Log.info("Label- Opcode: " + labelNode.getOpcode() + " Type: " + labelNode.getType());
			}
			else if (node instanceof LdcInsnNode)
			{
				LdcInsnNode ldcNode = (LdcInsnNode) node;
				ModInfo.Log.info("LdcInsn- Opcode: " + ldcNode.getOpcode() + " Type: " + ldcNode.getType());
			}
			else if (node instanceof LineNumberNode)
			{
				LineNumberNode lineNode = (LineNumberNode) node;
				ModInfo.Log.info("LineNumber- Line: " + lineNode.line + " Opcode: " + lineNode.getOpcode() + " Type: " + lineNode.getType());
			}
			else if (node instanceof LookupSwitchInsnNode)
			{
				LookupSwitchInsnNode lookupSwitch = (LookupSwitchInsnNode) node;
				ModInfo.Log.info("LookupSwitch- Opcode: " + lookupSwitch.getOpcode() + " Type: " + lookupSwitch.getType());
			}
			else if (node instanceof MultiANewArrayInsnNode)
			{
				MultiANewArrayInsnNode multiNode = (MultiANewArrayInsnNode) node;
				ModInfo.Log.info("MultiANewArray- Desc: " + multiNode.desc + " Dims: " + multiNode.dims + " Opcode: " + multiNode.getOpcode() + " Type: " + multiNode.getType());
			}
			else if (node instanceof TableSwitchInsnNode)
			{
				TableSwitchInsnNode tableSwitch = (TableSwitchInsnNode) node;
				ModInfo.Log.info("TableSwitch- Min: " + tableSwitch.min + " Max: " + tableSwitch.max + " Opcode:" + tableSwitch.getOpcode() + " Type: " + tableSwitch.getType());
			}
			else if (node instanceof TypeInsnNode)
			{
				TypeInsnNode typeNode = (TypeInsnNode) node;
				ModInfo.Log.info("TypeInsn- Desc: " + typeNode.desc + " Opcode: " + typeNode.getOpcode() + " Type: " + typeNode.getType());
			}
		}
	}
}