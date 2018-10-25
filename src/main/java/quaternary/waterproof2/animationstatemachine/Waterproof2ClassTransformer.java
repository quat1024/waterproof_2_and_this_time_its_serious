package quaternary.waterproof2.animationstatemachine;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class Waterproof2ClassTransformer implements IClassTransformer, Opcodes {
	private static final String liquidClassName = "net.minecraft.block.BlockDynamicLiquid";
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if(basicClass == null || !transformedName.equals(liquidClassName)) return basicClass;
		
		ClassReader reader = new ClassReader(basicClass);
		ClassNode node = new ClassNode();
		reader.accept(node, 0);
		
		done:
		for(MethodNode method : node.methods) {
			if(method.name.equals("canFlowInto") || method.name.equals("func_176373_h")) {
				InsnList instructions = method.instructions;
				
				for(int i = 0; i < instructions.size(); i++) {
					AbstractInsnNode instruction = instructions.get(i);
					if(instruction.getOpcode() == IRETURN) {
						InsnList hook = new InsnList();
						hook.add(new VarInsnNode(ALOAD, 3)); //IBlockState
						hook.add(new MethodInsnNode(
										INVOKESTATIC,
										"quaternary/waterproof2/Waterproof2",
										"canFlowInto_hook",
										"(ZLnet/minecraft/block/state/IBlockState;)Z",
										false
						));
						
						instructions.insertBefore(instruction, hook);
						break done;
					}
				}
			}
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		node.accept(writer);
		return writer.toByteArray();
	}
}
