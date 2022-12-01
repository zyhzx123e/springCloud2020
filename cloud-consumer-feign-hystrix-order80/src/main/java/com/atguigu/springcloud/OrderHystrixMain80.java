package com.atguigu.springcloud;

import com.atguigu.springcloud.config.BeanNameGen;
import com.atguigu.springcloud.entity.SecureString;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.file.Files;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.Driver;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Collectors;

//import method from class in package
import static com.atguigu.springcloud.service.TestService.genericInvokeMethod;
import static com.atguigu.springcloud.service.TestService.c;
import static smart.card.SmartCard1.*;

class ListNode {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 }



 /*MergeSort
 ultimately bubble sort quickly becomes less efficient when it comes to sorting
 larger data sets (or 'big data'). Where as, Merge Sort becomes more efficient as
 data sets grow

 Merge Sort is considered to be one of the fastest sorting algorithms,
 it is a bit more complex than Selection and Bubble Sort but its more efficient.
 The idea of Merge Sort is to divide the data-set into smaller data-sets,
 sort those smaller data-sets and then join them (merge them) together

 tested sort 10million int arr: 3seconds [merge sort]
 100million int arr: <30s
 * */
class MergeSort_QuickSort{

    static void deque(){
        //int arr[]=new int[]{1,5,2,6};
        //List<Integer> collect = Arrays.stream(arr).boxed().collect(Collectors.toList());

        LinkedList<Integer> queue = new LinkedList<>();
        //queue push to front
        //queue add to end
        //queue.push(1);//1 is the front
        //queue.push(2);//2 is the front
        queue.add(1);//1 is the front
        queue.add(2);//2 is the end
        System.out.println(queue);
        Iterator<Integer> iterator = queue.iterator();
        while (iterator.hasNext()){//checkForComodification
            Integer a=iterator.next();
            //int polstart= queue.poll();
            //int polend= queue.pollLast();//concurrentModification
            iterator.remove();
            System.out.println("polling : "+a);
        }
        System.out.println(queue);
    }

     public static void main(String[] args) {
         //deque();

         HashMap<String, BigDecimal> firstMap = new HashMap<>();
         firstMap.put("USD", new BigDecimal(11.22).setScale(2,BigDecimal.ROUND_HALF_EVEN));

            //map 2
         HashMap<String, BigDecimal> secondMap = new HashMap<>();
         secondMap.put("SGD", new BigDecimal(100.45).setScale(2,BigDecimal.ROUND_HALF_EVEN)); //It will replace D with F
         secondMap.put("USD", new BigDecimal(11.33).setScale(2,BigDecimal.ROUND_HALF_EVEN)); //A new pair to be added

        //Merge maps
         firstMap.forEach(
                 (key, value) -> secondMap.merge(key, value, (v1, v2) -> v1.add(v2).setScale(2,BigDecimal.ROUND_HALF_EVEN))
         );

         System.out.println("secondMap:"+secondMap);

         BigDecimal b1= BigDecimal.valueOf(0.003);
         BigDecimal b2= BigDecimal.valueOf(0.307);
         System.out.println(b1.add(b2).setScale(2, RoundingMode.CEILING));


         BigDecimal bd = new BigDecimal(5000);
         System.out.println(bd.setScale(2,BigDecimal.ROUND_UP));

         int mid_odd=0+(3-0)/2;//takes floor
         int mid_even=0+(4-0)/2;
         System.out.println("mid_odd:"+mid_odd);
         System.out.println("mid_even:"+mid_even);

         StringBuilder builder = new StringBuilder("Leaves growing");
         do {builder.delete(0, 5); } //start - end
         while (builder.length() > 5);
         System.out.println(builder);

         int[] arr_=new int[]{10,0,0,1,9,3,5};
         mergeSort(arr_,false);
         //quickSort(arr_,0,arr_.length-1,false);
         System.out.println(Arrays.toString(arr_));
     }

     //mergeSort time complexity worst scenario: O(nlogn)
     //mergeSort space complexity : O(n)
     static void mergeSort(int[] arrOri,boolean isAsc){

         //Arrays.sort(Arrays.stream(arrA_).boxed().toArray(), Collections.reverseOrder());
         int lenA=arrOri.length;
         int left=0,right=lenA;
         int mid=left+(right-left)/2;
         if(lenA==1){
             return;//base case
         }
         int[] leftArr=new int[mid];
         int[] rightArr=new int[lenA-mid];

         for(int i=-1;++i<mid;){
             leftArr[i]=arrOri[i];
         }
         for(int i=mid;i<lenA;++i){
             rightArr[i-mid]=arrOri[i];
         }

         mergeSort(leftArr,isAsc);
         mergeSort(rightArr,isAsc);

         int[] intsTmp = mergeTwoSortedArr(arrOri, leftArr, rightArr, isAsc);
         System.out.println("tmp arr :"+Arrays.toString(intsTmp));


     }

    static int[] mergeTwoSortedArr(int[] finalArr,int[] arrA,int[] arrB,boolean isAsc){
        //assume arrA and arrB are both sorted
        //base case is if both array has only 1 element
        int lenA=arrA.length;
        int lenB=arrB.length;
        //final int[] finalArr=new int[lenA+lenB];
        int pointer=0;
        for(int i=0,j=0; pointer<finalArr.length && (i<lenA || j<lenB); ){
            //System.out.println("pointer: "+pointer);
            //System.out.println("finalArr: "+Arrays.toString(finalArr));
            if(i!=lenA && j!=lenB){
                 if(arrA[i]<=arrB[j]){
                     //System.out.println("arrA["+i+"]<=arrB["+j+"]: ");
                     finalArr[pointer]= isAsc? arrA[i]:arrB[j];

                     ++pointer;
                     //finalArr[pointer]= isAsc? arrB[j]:arrA[i];++pointer;
                     if(isAsc)
                         ++i;
                     else
                         ++j;
                     continue;
                 }
                 else{
                     //System.out.println("arrA["+i+"]<=arrB["+j+"] else");
                     finalArr[pointer]= isAsc? arrB[j]:arrA[i];

                     ++pointer;
                     //finalArr[pointer]= isAsc? arrA[i]:arrB[j];++pointer;
                     if(isAsc)
                         ++j;
                     else
                         ++i;

                     continue;
                 }
                //++i;++j;
            }
            else if(i!=lenA && j==lenB){
                //System.out.println("i!=lenA: "+i);

                finalArr[pointer]= arrA[i];
                ++i;++pointer;
            }
            else if(i==lenA && j!=lenB){
                //System.out.println("j!=lenB: "+j);

                finalArr[pointer]= arrB[j];
                ++j;++pointer;
            }

        }

        return finalArr;
    }

    //quickSort time complexity avg scenario: O(nlogn), worst : O(n^2)
    //quickSort space complexity : O(n) in place
    static void quickSort(int[] arr,int lowIndex, int highIndex,boolean isAsce){

        if(lowIndex>=highIndex){
            System.out.println("base case hit[lowIndex:"+lowIndex+"] [highIndex:"+highIndex+"]-> "+Arrays.toString(Arrays.copyOfRange(arr, lowIndex, highIndex + 1)));
            return;//base case -> single arr
        }
        //1.decide a pivot
        int pivot=arr[highIndex];//u can grab any position as pivot

        //2.partitioning
        int partitionIndex = getPartitionIndex(arr, lowIndex, highIndex, pivot,isAsce);

        //3.recursive call on left & right sub array(note this did not create new array,
        // space complexity is good, in-place processing)
        quickSort(arr,lowIndex,partitionIndex-1,isAsce);
        quickSort(arr,partitionIndex+1,highIndex,isAsce);


    }

     private static int getPartitionIndex(int[] arr, int lowIndex, int highIndex, int pivot,boolean isAsce) {
         int leftPointer= lowIndex;
         int rightPointer= highIndex;

         while(leftPointer < rightPointer){
             if(isAsce){
                 while (arr[leftPointer]<= pivot && leftPointer < rightPointer){
                     ++leftPointer;
                 }
                 while (arr[rightPointer]>= pivot && leftPointer < rightPointer){
                     --rightPointer;
                 }
             }else{
                 while (arr[leftPointer]>= pivot && leftPointer < rightPointer){
                     ++leftPointer;
                 }
                 while (arr[rightPointer]<= pivot && leftPointer < rightPointer){
                     --rightPointer;
                 }
             }

             swap(arr,leftPointer,rightPointer);
         }
         swap(arr,leftPointer, highIndex);
         return leftPointer;
     }

     static void swap(int[] arr,int i,int j){
        int tmp=arr[i];
        arr[i]=arr[j];
        arr[j]=tmp;
    }
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class TreeNode {//bfs

    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int val){
        val=val;
        left=right=null;
    }
    ////BFS -> queue
    static int maxDepth(TreeNode root){//BFS -> queue
        if(root==null)return 0;

        int depth=0;
        Queue<TreeNode> queue=new LinkedList<>();
        queue.offer(root);//array.push

        while (!queue.isEmpty()){
            int size=queue.size();
            for(int i=-1;++i<size;){
                TreeNode cur=queue.poll();//array.shift
                if(null!=cur.left){
                    queue.offer(cur.left);
                }
                if(null!=cur.right){
                    queue.offer(cur.right);
                }
            }
            ++depth;
        }

        return depth;
    }

    ////DFS - bottom up recursive
    static int maxDepth_DFS(TreeNode root){//DFS -> queue
        if(root==null)return 0;
        //Time complexity: O(n)

        int left=maxDepth_DFS(root.left);
        int right=maxDepth_DFS(root.right);
        int depthMax=Math.max(left,right)+1;
        return depthMax;
    }


    //DFS - Binary Tree Maximum Path Sum
    public static int maxPathSum_total=Integer.MIN_VALUE;
    static int maxPathSum(TreeNode root){
        maxPathSum_DFS(root);
        return maxPathSum_total;
    }
    static int maxPathSum_DFS(TreeNode root){
        if(root==null)return 0;
        int left=maxPathSum_DFS(root.left);
        int right=maxPathSum_DFS(root.right);
        left=left<0?0:left;
        right=right<0?0:right;
        maxPathSum_total=Math.max(maxPathSum_total,(left+right+root.val));
        return Math.max((left+root.val),(right+root.val));
    }


    static List<List<Integer>> levelOrder(TreeNode root){
        List<List<Integer>> res=new ArrayList<>();
        if(root==null)return res;

        Queue<TreeNode> queue=new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()){
            int size=queue.size();
            List<Integer> level=new ArrayList<>();
            for(int i=-1;++i<size;){
                TreeNode cur=queue.poll();
                level.add(cur.val);
                if(null!=cur.left){
                    queue.offer(cur.left);
                }
                if(null!=cur.right){
                    queue.offer(cur.right);
                }
            }
            res.add(level);
        }

        return res;
    }

    static List<Integer> rightSideView(TreeNode root){
        List<Integer> res=new ArrayList<>();
        if(root==null)return res;

        Queue<TreeNode> queue=new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()){
            int size=queue.size();

            res.add(queue.peek().val);

            for(int i=-1;++i<size;){
                TreeNode cur=queue.poll();

                if(null!=cur.right){
                    queue.offer(cur.right);
                }//right first bcz we need peek the 1st one
                if(null!=cur.left){
                    queue.offer(cur.left);
                }

            }

        }

        return res;
    }


    //1.preOrder DFS
    static void preOrderDFS(TreeNode node){
        if(node==null)return;

        System.out.println("preOrderDFS : node->"+node.val);
        preOrderDFS(node.left);
        preOrderDFS(node.right);
    }
    //2.inOrder DFS
    static void inOrderDFS(TreeNode node){
        if(node==null)return;

        inOrderDFS(node.left);
        System.out.println("inOrderDFS : node->"+node.val);
        inOrderDFS(node.right);
    }
    //3.postOrder DFS
    static void postOrderDFS(TreeNode node){
        if(node==null)return;

        postOrderDFS(node.left);
        postOrderDFS(node.right);
        System.out.println("postOrderDFS : node->"+node.val);
    }


    //1.leetcode solution1: Print permutation array set of a given int array
    public List<List<Integer>> permute1(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        helperPermute(res, nums, new HashSet<Integer>(), new LinkedList<Integer>());
        return res;
    }

    private static void helperPermute(List<List<Integer>> res, int[] nums,
                                      Set<Integer> set, List<Integer> curr) {
        if (curr.size() == nums.length) {
            res.add(new ArrayList<>(curr));
            return;
        }
        for (int num : nums) {
            if (set.contains(num)) continue;
            set.add(num);
            curr.add(num);
            helperPermute(res, nums, set, curr);
            set.remove(num);
            curr.remove(curr.size() - 1);
        }
    }


    public int[] removeEleFromArr(int[] nums,int i){
        int l=nums.length;
        List<Integer> newList=new ArrayList<>();
        for(int j=-1;++j<l;){
            if(j!=i){
                newList.add(nums[j]);
            }
        }
        return newList.stream().mapToInt(Integer::intValue).toArray();
    }
    public List<List<Integer>> permuteA(int[] nums) {
        List<List<Integer>> subresult=new LinkedList<>();

        int l=nums.length;

        for(int i=-1;++i<l;){

            int parent=nums[i];
            //int[] newL=removeEleFromArr(nums,i);
            int[] newL=new int[nums.length-1];

            System.arraycopy(nums, 0, newL, 0, i);

            System.arraycopy(nums, i+1, newL, i, nums.length-i-1);

            //System.out.println(Arrays.toString(newL));
            List<List<Integer>> lAll=permuteA(newL);

            for(List<Integer> il:lAll){
                il.add(0,parent);
                subresult.add(il);
            }
        }

        if(l==0){
            subresult.add(new ArrayList<Integer>());
        }

        return subresult;
    }

    //leetcode 904. Fruit into Baskets
    public int totalFruit(int[] fruits) {
        int j=0,max=0;
        Map<Integer,Integer> map=new HashMap<>();

        //ClassUtils.
        for(int i=-1,l=fruits.length;++i<l;){
            map.put(fruits[i],i);
            if(map.size()>2){
                int minL=l-1;
                Comparator<Integer> comparator= Comparator.comparing(Integer::intValue);
                Optional<Integer> oi=map.entrySet().stream().map(m->m.getValue()).min(comparator);

                minL=oi.orElse(l-1);
//                for(java.util.Map.Entry<Integer,Integer> ent: map.entrySet()){
//                    int key_=ent.getKey();
//                    int val_=ent.getValue();
//                    if(val_<minL){
//                        minL=val_;
//                    }
//                }
                map.remove(fruits[minL]);
                j=minL+1;
            }
            max=Math.max(max,i-j+1);
        }
        return max;
    }

    //leetcode 20.Valid Parentheses
    public boolean isValidParenteses(String s) {
        Map<Character,Character> mapP=new HashMap<>();
        mapP.put('(',')');
        mapP.put('[',']');
        mapP.put('{','}');
        Stack<Character> stack=new Stack<>();
        for(int i=-1,l=s.length();++i<l;){
            if(mapP.containsKey(s.charAt(i))){
                stack.push(mapP.get(s.charAt(i)));
            }else{
                if(stack.isEmpty() || stack.pop()!=s.charAt(i) ){
                  return false;
                }
            }
        }

        if(stack.size()>0)
            return false;
        else
            return true;
    }


    //1.leetcode solution2: prefix
    public List<List<Integer>> permute(int[] nums) {
        List<Integer> list = Arrays.stream(nums)
                .boxed().collect(Collectors.<Integer>toList());
        return calculatePermutation(list);
    }
    public List<List<Integer>> calculatePermutation(List<Integer> nums){
        System.out.println(nums);
        List<List<Integer>> subresult=new LinkedList<>();

        int len=nums.size();
        for (int i = -1; ++i < len;) {

            int prefix=nums.get(i).intValue();

            List<Integer> copyArr=new LinkedList<>(nums);
            copyArr.remove(i);
            //ArrayUtils.remove()
            // Arrays.copyOf()

            List<List<Integer>> arr_= calculatePermutation(copyArr);

            if(arr_.size()>0){
                for (List<Integer> li:arr_){
                    li.add(0,prefix);
                    subresult.add(li);
                }
            }else{
                subresult.add(new LinkedList<>(Arrays.asList(prefix)));
            }


        }

        return subresult;
    }


    //1.leetcode permutation II: Unique permutation
    List<List<Integer>> mainlist=new LinkedList<>();
    int unitLen;
    public List<List<Integer>> permuteUnique(int[] nums) {
        List<Integer> list = Arrays.stream(nums)
                .boxed().collect(Collectors.<Integer>toList());
        return calculatePermutationUnique(list);
    }
    public List<List<Integer>> calculatePermutationUnique(List<Integer> nums){

        List<List<Integer>> subresult=new LinkedList<>();

        int len=nums.size();
        for (int i = -1; ++i < len;) {

            int prefix=nums.get(i).intValue();

            List<Integer> copyArr=new LinkedList<>(nums);
            copyArr.remove(i);

            List<List<Integer>> arr_= calculatePermutation(copyArr);

            if(arr_.size()>0){
                for (List<Integer> li:arr_){
                    li.add(0,prefix);
                    subresult.add(li);
//                    if(li.size()==unitLen){
//                        List<Integer> ll=mainlist.stream()
//                                .filter(f->f.equals(li)).findFirst().
//                                orElse(new LinkedList<>());
//                        if(ll.size()<1){
//                            mainlist.add(li);
//                        }
//                    }
                }
            }else{
                //System.out.println(prefix);
                subresult.add(new LinkedList<>(Arrays.asList(prefix)));
            }


        }

        subresult=subresult.stream().distinct().collect(Collectors.toList());

        return subresult;
    }

    //leetcode combination,
    //Given two integers n and k, return all possible combinations of k numbers out of the range [1, n].
    public static List<List<Integer>> combination_(int n, int k) {
        List<List<Integer>> ans = new LinkedList <>();

        if(k == 0)
        {
            ans.add(new LinkedList<>());
            return ans;
        }
        backtrackCom(1,new LinkedList <>(),n,k,ans);
        return ans;

    }

    public static void backtrackCom(int start, LinkedList<Integer> current, int n, int k, List<List<Integer>> ans)
    {
        if(current.size() == k)
        {
            ans.add(new LinkedList(current));
            return;
        }
        for(int i = start ; i<=n;i++)
        {
            current.add(i);
            System.out.println("backtrackCom current ("+i+"):"+current);
            backtrackCom(i+1,current,n,k,ans);
            current.removeLast();
            System.out.println("backtrackCom current ("+i+") after removeLast:"+current);
        }
    }

    //1.smallest substring of all char[]
    public static void getShortestUniqueSubstring(char[] arr, String str){
        int strlen=str.length();

        char[] strArr=str.toCharArray();


        Map<Character,ArrayList<Integer>> mapstr=new HashMap<>();
        for (int i = -1; ++i < strlen;) {
            char stri=strArr[i];
            if(ArrayUtils.indexOf(arr, stri)>-1){
                List<Integer> lis=mapstr.computeIfAbsent(stri,k->new ArrayList<>());
                lis.add(i);
            }
        }
        System.out.println("getShortestUniqueSubstring mapstr:"+mapstr);

    }


    //leetcode 141.linked cycle list
    public boolean hasCycle(ListNode head) {

        ListNode curr = head;

        //Map<String,Integer> map=new HashMap<>();
        while(curr!=null){
            if(curr.val==Integer.MIN_VALUE)
                return true;
            else{
                curr.val=Integer.MIN_VALUE;
                curr = curr.next;
            }
        }

        return false;
    }


    public boolean hasCycle2(ListNode head) {

        ListNode curr = head;
        Map<Integer,Integer> map=new HashMap<>();
        while(curr!=null){
            if(map.containsKey(curr.hashCode()))
                return true;
            else{
                map.put(curr.hashCode(),curr.val);
                curr = curr.next;
            }
        }

        return false;
    }


    //Leetcode 15.3sum
    //Given an integer array nums, return all the triplets [nums[i], nums[j], nums[k]]
    // such that i != j, i != k, and j != k, and nums[i] + nums[j] + nums[k] == 0.
    //Notice that the solution set must not contain duplicate triplets.
    public List<List<Integer>> threeSumSlow(int[] nums) {
        List<List<Integer>> lia=new ArrayList<>();


        //Map<Integer,List<Integer>> mli=new HashMap<>();
        int len=nums.length;
        if(len<3){
            return lia;
        }
        for(int i=-1;++i<len;){
            for(int j=-1; ++j<len;){
                for(int k=-1; ++k<len;){//&& (1+j)<len
                    final int f_j=j;final int f_i=i;final int f_k=k;
                    if(i!=j && (k!=i) && (j!=k) && 0==(nums[i]+nums[j]+nums[k])){

                        //                    List<Integer> ltmp=mli.computeIfAbsent(i,k->new ArrayList<>());
                        //                    ltmp.add(nums[j]);
                        //                    ltmp.add(nums[1+j]);

                        if(nums[f_i]==nums[f_j] && nums[f_i]==nums[f_k]
                                && nums[f_j]==nums[f_k]){

                            List<Integer> nlis=Arrays.asList(nums[f_i],nums[f_j],nums[f_k]);
                            List<List<Integer>> ltmpF= lia.stream()
                                    .filter(f->listEqualsIgnoreOrder(f,nlis) )
                                    .collect(Collectors.toList());

                            if(ltmpF!=null && ltmpF.size()>0){
                                //System.out.println("duplicated triplets");

                            }else{
                                System.out.println("adding triplets nums["+i+"]:"+nums[i]);
                                System.out.println("adding triplets nums["+j+"]:"+nums[j]);
                                System.out.println("adding triplets nums["+(k)+"]:"+nums[k]);
                                List<Integer> ltmp=new ArrayList<>();
                                ltmp.add(nums[i]); ltmp.add(nums[j]);ltmp.add(nums[k]);
                                lia.add(ltmp);
                            }


                        }else{
                            List<List<Integer>> ltmpF= lia.stream()
                                    .filter(f->f.indexOf(nums[f_i])>-1 &&
                                            f.indexOf(nums[f_j])>-1 &&
                                            f.indexOf(nums[f_k])>-1)
                                    .collect(Collectors.toList());

                            if(ltmpF!=null && ltmpF.size()>0){
                                //System.out.println("duplicated triplets");

                            }else{
                                System.out.println("adding triplets nums["+i+"]:"+nums[i]);
                                System.out.println("adding triplets nums["+j+"]:"+nums[j]);
                                System.out.println("adding triplets nums["+(k)+"]:"+nums[k]);
                                List<Integer> ltmp=new ArrayList<>();
                                ltmp.add(nums[i]); ltmp.add(nums[j]);ltmp.add(nums[k]);
                                lia.add(ltmp);
                            }


                        }

                    }
                }
            }
        }
        System.out.println("threeSum lia -> "+lia);


        return lia;
    }

    private static <T> boolean listEqualsIgnoreOrder(List<T> list1, List<T> list2) {
        if (list1 == null)
            return list2==null;
        if (list2 == null)
            return list1 == null;
        return new HashSet<>(list1).equals(new HashSet<>(list2));
    }



    //leetcode 3sum: 2 pointer
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> res = new ArrayList<>(); // resultant array
        if (nums == null || nums.length < 3) {
            return res;
        }
        Arrays.sort(nums); // sort array so that we can avoid duplicate
        for (int i = 0; i < nums.length - 2; i++) {
            if (i == 0 || nums[i] > nums[i-1]) { // initial, i=0, or nums[i]>nums[i-1] then start i.e. avoid dups
                int j = i + 1; // second pointer
                int k = nums.length - 1; // 3rd pointer

                while (j < k) {
                    if (nums[i]+nums[j]+nums[k] == 0) { // i+j+k == 0, we get result
                        List<Integer> list = new ArrayList<>();
                        list.add(nums[i]);
                        list.add(nums[j]);
                        list.add(nums[k]);
                        res.add(list);
                        j++;
                        k--;
                        while (j < k && nums[j] == nums[j-1]) { // avoid dups
                            j++;
                        }
                        while (j < k && nums[k] == nums[k+1]) { // avoid dups
                            k--;
                        }

                    } else if (nums[i]+nums[j]+nums[k] < 0) { // less than 0 then increment j
                        j++;
                    } else { // i+j+k> 0 then decrement k
                        k--;
                    }
                }
            }
        }
        return res; // return res
    }

    //leetcode 3sum hashset
    public List<List<Integer>> threeSumHashSet(int[] nums) {
        Set<List<Integer>> ans = new HashSet<>();

        for (int i=0; i<nums.length-2; i++) {
            Set<Integer> targetOccurence = new HashSet<>();
            int target = -nums[i];
            for (int j=i+1; j<nums.length; j++) {
                if (targetOccurence.contains(target - nums[j])) {
                    /*Found answer*/
                    final List<Integer> triplet = Arrays.asList(nums[i], nums[j], target-nums[j]);
                    Collections.sort(triplet);
                    ans.add(triplet);
                } else {
                    targetOccurence.add(nums[j]);
                }
            }
        }
        return new ArrayList<>(ans);
    }


    //leetcode k merge list
    public ListNode mergeKLists(ListNode[] lists) {
        ListNode dummy = new ListNode();
        ListNode iter = dummy;
        PriorityQueue<ListNode> pq = new PriorityQueue<>((a, b) -> a.val - b.val);
        for (ListNode node : lists) {
            if (node != null) {
                pq.offer(node);
            }
        }
        while(!pq.isEmpty()) {
            ListNode node = pq.poll();
            iter.next = node;
            iter = iter.next;
            if (node.next != null) {
                pq.offer(node.next);
            }
        }
        return dummy.next;
    }



    //leetcode 3. Longest Substring Without Repeating Characters
    public int lengthOfLongestSubstring(String s) {
        int countl=0;
        int len=s.length();
        if(len<2){
            return len;
        }

        //Map<String,Integer> mapC=new HashMap<>();
        StringBuilder longestStrSoFar=new StringBuilder();
        outer:for(int q=-1;++q<(len-1);){
            int pointer1=q+1;
            StringBuilder sb=new StringBuilder();
            sb.append(s.charAt(q));


            blockA:{
                inner:while(pointer1<len){
                    String tmc=String.valueOf(s.charAt(pointer1));
                    if(sb.indexOf(tmc)<0){//non repeating
                        sb.append(tmc);
                    }else{
                        // q=pointer1-1;
                        break blockA;
                        //break inner;
                    }

                    ++pointer1;
                }
            }

            System.out.println("get sb.length():"+sb.length()+"|");
            if(sb.length()>countl){

                longestStrSoFar=sb;

                countl=sb.length();


            }

        }
        return countl;
    }

    //leetcode 3.Longest Substring Without Repeating Characters (hashmap fastest)
    public int longestSubstringLeetcode(String s){
        int l = s.length();
        if (l==0) return l;
        int maxLen=0,right=0,left=0;
        HashMap<Character, Integer> map = new HashMap<>();

        while(right < l){
            if(map.containsKey(s.charAt(right))){
                left = Math.max(map.get(s.charAt(right))+1, left);
            }

            map.put(s.charAt(right), right);

            maxLen = Math.max(maxLen, right-left + 1);

            ++right;
        }
        return maxLen;

    }

    public int longestSubstringHashset(String s){
        int i=0,j=0;
        int l=s.length();
        int maxLen=0;
        if (l==0)return l;

        Set<Character> set=new HashSet<>();
        while(i<l){
            if(!set.contains(s.charAt(i))){
                set.add(s.charAt(i));
                maxLen=Math.max(set.size(),maxLen);
            }else{
                while(set.contains(s.charAt(i))){
                    set.remove(s.charAt(j));
                    ++j;
                }
                set.add(s.charAt(i));
            }

            ++i;
        }
        return maxLen;
    }


    //leetcode 654. constructMaximumBinaryTree
    public TreeNode constructMaximumBinaryTree(int[] nums) {
        TreeNode root=null;
        int l=nums.length;
        if(l<1){
            return null;
        }
        int maxPointer=nums[0];
        //maxPointer=Arrays.stream(nums).max().getAsInt();

        for(int i=l;--i>-1;){
            if(maxPointer<nums[i]){
                maxPointer=i;
            }
        }
        System.out.println("maxPointer->"+maxPointer);
        root=new TreeNode(nums[maxPointer]);
        int[] lArr=Arrays.copyOfRange(nums, 0, maxPointer);
        int[] rArr=Arrays.copyOfRange(nums, maxPointer+1, l);

        root.left=constructMaximumBinaryTree(lArr);
        root.right=constructMaximumBinaryTree(rArr);

        return root;
    }

    //leetcode 5.Longest Palindromic Substring start
    int maxLen=1;
    int leftPalindrome=0;
    public String longestPalindrome(String s) {

        int len=s.length();
        if(len<2){
            return s;
        }

        for(int i=-1;++i<len;){
            //1.case : odd num str
            longestPalindromExpandFromCenter(i-1,i+1,s);//odd num str
            //2.case : even num str
            longestPalindromExpandFromCenter(i,i+1,s);//even num str
        }

        return s.substring(leftPalindrome,leftPalindrome+maxLen);
    }

    public void longestPalindromExpandFromCenter(int start,int end,String sCache){
        int len=sCache.length();
        while(start>=0 && end<len && sCache.charAt(start)==sCache.charAt(end)){
            if(end-start+1 > maxLen){
                maxLen=end-start+1;
                leftPalindrome=start;
            }
            --start;
            ++end;
        }

    }

    //leetcode 5.Longest Palindromic Substring end

    //leetcode 48. rotate image
    public int[][] rotate(int[][] matrix) {
        int len=matrix.length;
        int[][] matrixF=new int[len][len];

        int q=0,p=0;
        for(int i=len;--i>-1;++p){
            for(int j=-1;++j<len;){
                matrixF[p][j]=matrix[len-1-j][p];
            }
        }
        return matrixF;
    }


    //leetcode combination
    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> result = new LinkedList<>();
        if(k==0){
            result.add(new LinkedList());
            return result;
        }
        comb(1,new LinkedList<Integer>(),n,k,result);
        return result;
    }
    public void comb(int start,LinkedList<Integer> current,int n,int k,List<List<Integer>> result){
        if(current.size()==k){
            result.add(new LinkedList(current));
        }
        for(int i=start;i<=n && current.size()<k;i++){
            current.add(i);
            comb(i+1,current,n,k,result);
            current.removeLast();
        }
    }
}  

//1. LRU LinkedHashMap version LRU Cache
class LRUCache<K,V> extends LinkedHashMap<K,V> {

    public int maxCapacity;
    public LRUCache(int maxCapacity){
        super(maxCapacity,0.75f,true);
        this.maxCapacity=maxCapacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return super.size()>this.maxCapacity;
    }

    private void printAk(){
        System.out.println("printAk");
    }

    public static void main(String[] args) {




        LRUCache lru=new LRUCache(3);
        lru.put(1,"a");
        lru.put(2,"a");
        lru.put(3,"a");
        System.out.println(lru.keySet());
        lru.put(4,"d");
        System.out.println(lru.keySet());
        lru.put(3,"c");
        lru.put(3,"c");
        lru.put(3,"c");
        System.out.println(lru.keySet());
        genericInvokeMethod(lru,"printAk");

    }

//    public int get(int key) {
//
//    }
//
//    public void put(int key, int value) {
//
//    }
}

//2. Custom LRU Cache
class LRUCacheDemo {


    class Node<K,V>{
        K key;
        V value;
        Node<K,V> next;
        Node<K,V> prev;
        public Node(){
            this.next=this.prev=null;
        }
        public Node(K key,V value){
            this.key=key;
            this.value=value;
            this.next=this.prev=null;
        }

    }
    public Node reverNodeList(Node curNode){
        Node cur=curNode;
        Node prev=null;
        while(cur!=null){
            Node tmp=cur.next;
            cur.next=prev;
            prev=cur;
            cur=tmp;
        }
        return prev;
    }


    //doublely linked list like AQS
    class DoublelyLinkedList<K,V>{
        Node<K,V> head;
        Node<K,V> tail;

        public DoublelyLinkedList(){
            head=new Node<>();
            tail=new Node<>();
            head.next=tail;
            tail.prev=head;
        }

        public void addHead(Node<K,V> node){
            node.next=head.next;
            node.prev=head;
            head.next=node;
            head.next.prev=node;

        }

        public void removeNode(Node<K,V> node){
            node.prev.next=node.next;
            node.next.prev=node.prev;
            node.next=null;
            node.prev=null;
        }

        public Node getLast(){
            return tail.prev;
        }

    }


    public int maxSize;
    public Map<Integer,Node<Integer,Integer>> map;//=new HashMap<>();
    public DoublelyLinkedList<Integer,Integer> doublelyLinkedList;

    public LRUCacheDemo(int maxSize){
        this.maxSize=maxSize;
        map=new HashMap<>();
        doublelyLinkedList=new DoublelyLinkedList<>();
    }

    public int get(int key){
        if(!map.containsKey(key)){
            return -1;
        }
        Node<Integer,Integer> node= map.get(key);
        doublelyLinkedList.removeNode(node);
        doublelyLinkedList.addHead(node);
        return node.value;
    }

    //save or update
    public void put(int key,int val){
        if(map.containsKey(key)){//update
            Node<Integer,Integer> node =map.get(key);
            node.value=val;
            doublelyLinkedList.removeNode(node);
            doublelyLinkedList.addHead(node);
            map.put(key,node);
        }else{
            if(map.size()>=maxSize){
                Node<Integer,Integer> lastNode = doublelyLinkedList.getLast();
                doublelyLinkedList.removeNode(lastNode);
                map.remove(key);
            }
            //add new
            Node<Integer,Integer> newNode =new Node<>(key,val);
            doublelyLinkedList.addHead(newNode);
            map.put(key,newNode);
        }
    }



    public static void main(String[] args) {
        Set<String> ad = new HashSet<String>() {{
            add("a");
            add("b");
        }};
        Set<String> bd =  new HashSet<String>() {{
            add("a");
        }};
        ad.removeAll(bd);
        System.out.println(ad);

        LRUCacheDemo lru=new LRUCacheDemo(3);
        lru.put(1,1);
        lru.put(2,2);
        lru.put(3,3);
        System.out.println(lru.map.keySet());
        lru.put(4,4);
        System.out.println(lru.map.keySet());
        lru.put(3,3);
        lru.put(3,3);
        lru.put(3,3);
        System.out.println(lru.map.keySet());
    }

}



class ZeroEvenOddBlockingQ {
    private int n;
    BlockingQueue<Integer> bq=new SynchronousQueue<>();//new LinkedBlockingDeque<>(1);


    volatile AtomicInteger stateC=new AtomicInteger(0);
    volatile AtomicBoolean stateB=new AtomicBoolean(false);
    public ZeroEvenOddBlockingQ(int n) {
//        try{
//            this.bq.put(0);
//        }catch(Exception e){e.printStackTrace();}
        System.out.println(" ZeroEvenOdd>>>>>>>>>>>>>>>>>>>>>>>>>>:"+n);

        this.n = n;
    }
    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {

        for (int i = -1; ++i < n;) {

            while(stateC.get()!=0){
                System.out.println(" zero while(stateC.get()!=0):"+i);
                bq.offer(i,0,TimeUnit.MILLISECONDS);
                //continue;
            }
            System.out.println(" zero while(stateC.get()!=0) then :"+i);
            System.out.println(" ===========================================0");
            printNumber.accept(0);
            if(stateC.get()==0){
                if(stateB.get()){
                    stateC.compareAndSet(0,2);//even
                }else{
                    stateC.compareAndSet(0,1);//odd
                }

                //if(bq.size()>0){
                System.out.println("zero bq take while bq.size():"+bq.size());
                bq.poll(0,TimeUnit.MILLISECONDS);
                //}
                System.out.println("zero bq take  bq.size():"+bq.size());
            }




            stateB.compareAndSet(stateB.get(),!stateB.get());


        }
    }


    public void odd(IntConsumer printNumber) throws InterruptedException {
        for (int i = -1; (i=i+2) <= n; ) {
            while(stateC.get()!=1){
                System.out.println(" odd while(stateC.get()!=1):"+i);
                bq.offer(i,0,TimeUnit.MILLISECONDS);
                //continue;
            }
            System.out.println(" odd while(stateC.get()!=1) then:"+i);
            System.out.println(" ==========================================="+i);
            printNumber.accept(i);
            if(stateC.get()==1){
                stateC.compareAndSet(1,0);//0
                //if(bq.size()>0){
                System.out.println("odd bq take while bq.size():"+bq.size());
                bq.poll(0,TimeUnit.MILLISECONDS);
                //}
                System.out.println("odd bq take bq.size():"+bq.size());
            }


        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        for (int i = 0; (i=i+2) <= n;) {
            while(stateC.get()!=2){
                System.out.println(" even while(stateC.get()!=2):"+i);
                bq.offer(i,0,TimeUnit.MILLISECONDS);
                //continue;
            }
            System.out.println(" even while(stateC.get()!=2) then :"+i);
            System.out.println(" ==========================================="+i);
            printNumber.accept(i);

            if(stateC.get()==2){
                stateC.compareAndSet(2,0);//0
                // if(bq.size()>0){
                System.out.println("even bq take while bq.size():"+bq.size());
                bq.poll(0,TimeUnit.MILLISECONDS);
                //}
                System.out.println("even bq take bq.size():"+bq.size());
            }



        }
    }



    public void threadPoolExecutor(String[] vv) throws ExecutionException, InterruptedException {

        final int numberOfProcessors = Runtime.getRuntime().availableProcessors();

        Double poolsize= (numberOfProcessors/(1-0.8));//I/O Intensive
        int poolsizeCPUintensive= (numberOfProcessors*2);//CPU Intensive

        ExecutorService executor = new ThreadPoolExecutor(3,
                poolsize.intValue(),
                300,TimeUnit.MICROSECONDS,
                new LinkedBlockingQueue<>(10),
                Executors.privilegedThreadFactory(),
                new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());
        Collection<Callable<Integer>> tasks = new ArrayList<>();   // our task do not need a returned value

        int len=vv.length;
        for ( int i=-1;++i<len;) {
            final int i_f=i;
            Callable<Integer> task = () -> {
                System.out.println("callable :"+vv[i_f]);
                Thread.sleep(1000);
                int b=1/0;
                //executor.shutdownNow();//shutdown immediately
                return i_f;
            };

            FutureTask<Integer> ft=new FutureTask<>(task);
            ft.run();
            //int geti=ft.get();

            tasks.add(task);
        }

        try {

            List<Future<Integer>> lf= executor.invokeAll(tasks);
            for (Future<Integer> li:lf) {
                try {
                    int result=li.get(100,TimeUnit.MICROSECONDS);
                    System.out.println("future get : "+result);
                }catch (Exception ec){
                    ec.printStackTrace();
                }

            }
        } catch(InterruptedException ex) {
            System.out.println(ex);
        }



        //
        System.out.println("ForkJoin pool demo1====================");

        final ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfProcessors);

        final ForkJoinTask<Integer> result = forkJoinPool.submit(new Fibonacci(30));

        System.out.println("The result is : " + result.join());

        System.out.println("ForjJoin pool compute demo2==================");

        System.out.println("numbers of core available in your processor:"  +numberOfProcessors);
        int[] n = {20 , 23 , 5 ,6 ,7,8,23,12,56,1};
        ForkJoinPool Pool = new ForkJoinPool(numberOfProcessors);
        Testl1 t=new Testl1(n,0,n.length);

        System.out.println(" queued Surplus task count :" + Testl1.getSurplusQueuedTaskCount() );
        System.out.println("   Before awaitTermination (is terminating):" +Pool.isTerminating() );
        System.out.println("  is awaitQuiescent :" + Pool.awaitQuiescence(2, TimeUnit.SECONDS) );

        Pool.awaitTermination(3, TimeUnit.SECONDS);
        System.out.println("   After awaitTermination (is terminating):" +Pool.isTerminating() );
        Integer r = Pool.invoke(t);
        System.out.println("Pool.invoke :" +r);

        StringBuilder sb_=new StringBuilder(16);//default 16 size, grow by [capacity]*2+2
        sb_.capacity();

        StringBuffer sb_s=new StringBuffer("abc");//size will be str.length() + 16
        sb_s.charAt(2);
        sb_s.setCharAt(2,sb_s.charAt(2));
        sb_s.append("3.13",1,2);
        sb_s.substring(0,1);
        sb_s.insert(0,3.14);

        List<Integer> lin=new ArrayList<>(256);//init 10 size, grow by [capacity]+([capacity]/2)
        //ArrayList:  int newCapacity = oldCapacity + (oldCapacity >> 1);

        lin.removeAll(lin);
        Set<String> ses=new HashSet<>(20);


    }


    static class Testl1 extends RecursiveTask<Integer> {
        int st;
        int end;
        int[] arr;

        Testl1(int[] arr, int st, int end) {
            this.arr = arr;
            this.st = st;
            this.end = end;
        }

        protected Integer compute() {
            if (st - end <= 10) {
                int sum = 0;
                for (int i = st; i < end; ++i)
                    sum += arr[i];
                return sum;
            } else {
                int mid = st + (end - st) / 2;
                Testl1 t1 = new Testl1(arr, st, mid);
                Testl1 t2 = new Testl1(arr, st, mid);
                invokeAll(t1, t2);
                return (t1.join() + t2.join());
            }
        }

    }

    static class Fibonacci extends RecursiveTask<Integer> {

        private final int number;

        public Fibonacci(int number) {
            this.number = number;
        }

        @Override
        protected Integer compute() {
            if (number <= 1) {
                return number;
            } else {
                Fibonacci fibonacciMinus1 = new Fibonacci(number - 1);
                Fibonacci fibonacciMinus2 = new Fibonacci(number - 2);
                fibonacciMinus1.fork();
                return fibonacciMinus2.compute() + fibonacciMinus1.join();
            }
        }
    }
}


class ZeroEvenOddSemaphore {
    private int n;
    private Semaphore zero = new Semaphore(1);
    private Semaphore even = new Semaphore(0);
    private Semaphore odd = new Semaphore(2);

    public ZeroEvenOddSemaphore(int n) {
        this.n = n;
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {

        for (int i = 0; i < n; i++) {
            zero.acquire();
            printNumber.accept(0);
            even.release();
            odd.release();

        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        for (int i = 2; i <= n; i+=2) {
            even.acquire(3);
            printNumber.accept(i);
            zero.release();
            odd.release();
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i+=2) {
            odd.acquire(3);
            printNumber.accept(i);
            zero.release();
            even.release();
        }
    }
}

class ZeroEvenOdd {
    private volatile int n;
    ReentrantLock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    final String[] methods = {"ZERO", "EVEN", "ODD"};
    private volatile String curentMethod = methods[0];
    public ZeroEvenOdd(int n) {
        this.n = n;
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i < n+1; i++)
            job(printNumber, 0, methods[0]);
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i < n+1; i++)
            job(printNumber, i, methods[1]);
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i < n+1; i++)
            job(printNumber, i, methods[2]);
    }

    void job(IntConsumer printN, int cou, String method) throws InterruptedException{
        try {
            lock.lock();
            while (!method.equals(curentMethod))
                condition.await();
            if (method.equals(methods[0])){
                curentMethod = methods[1];
                printN.accept(0);
            } else if (method.equals(methods[1])){
                curentMethod = methods[2];
                if (cou % 2 == 0)
                    printN.accept(cou);
            } else if (method.equals(methods[2])){
                curentMethod = methods[0];
                if (cou % 2 != 0)
                    printN.accept(cou);
            }
            condition.signalAll();
        } finally {
            lock.unlock();
        }

    }
}


class SharedData{
     /*
     1.synchronized: jvm lvl , java keyword
        bytecode: monitorenter & monitorexit(monitorexit has 2, normal termination & exceptional termination)
        bcz wait & notify oso rely on monitor object, so wait & notify must be used in synchronized block, else illegalMonitorStateException
        - synchronized no need manually release the lock, lock need(if lock didnt manually release, then deadlock will be occured)
        - synchronized cannot be interrupted, lock can use tryLock(Long timeout,TimeUnit unit), lockInterruptibly() then call interrupt() method to interrupt
        - synchronized default is unfair lock, Lock defaultt is unfair lock(can change to fair lock)
        - synchronized cannot bind multiple conditions, lock can awake specific thread with condition, not like synchronized(wither awake 1 or all)
     2.lock api level is a class(java.util.concurrrent.locks.Lock), after java 5
     3.
     * */
     private int number=0;
    private Lock lock=new ReentrantLock();

    private Condition condition = lock.newCondition();


    public void incrementLock() throws Exception{

        lock.lock();
        try {

            while(number!=0){

                //wait consumer to consume
                condition.await();
            }
            ++number;
            System.out.println(Thread.currentThread().getName()+"\t ++"+number);

            condition.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }



    }


    public void decrementLock() throws Exception{

        lock.lock();
        try {

            while(number==0){

                //wait consumer to consume
                condition.await();
            }
            --number;
            System.out.println(Thread.currentThread().getName()+"\t --"+number);

            condition.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }



    }


    //new Thread(()->{},"A").start();

    //use lockto awake 1 -> 2 -> 3, 1 by one in a cycle(rond robin)
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();
    
    public void print5(){
        lock.lock();
        try {
            while(number!=1){
                c1.await();
            }
            for (int i = 1; i <= 5; i++) {

                System.out.println(Thread.currentThread().getName()+"\t"+ i);
            }

            number=2;
            c2.signal();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public void print10(){
        lock.lock();
        try {
            while(number!=2){
                c2.await();
            }
            for (int i = 1; i <= 10; i++) {

                System.out.println(Thread.currentThread().getName()+"\t"+ i);
            }

            number=3;
            c3.signal();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public void print15(){
        lock.lock();
        try {
            while(number!=2){
                c3.await();
            }
            for (int i = 1; i <= 15; i++) {

                System.out.println(Thread.currentThread().getName()+"\t"+ i);
            }

            number=1;
            c1.signal();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }


    public final class blockingQConsumerProducer{

        private volatile boolean FLAG=true;
        private AtomicInteger atomicI=new AtomicInteger();

        BlockingQueue<String> blockingQ=null;
        public  blockingQConsumerProducer(BlockingQueue<String> bq_){
            this.blockingQ=bq_;
            System.out.println(bq_.getClass().getName());//reflection to get the impleted class
        }
        public void produce()throws Exception{
           String data=null;
           boolean retVal;
           while(FLAG){
                data=atomicI.incrementAndGet()+"";
                retVal= blockingQ.offer(data,2,TimeUnit.SECONDS);
                if(retVal){
                    System.out.println(Thread.currentThread().getName()+"\t insert blockingQ "+data+" succeeded ");
                }else {
                    System.out.println(Thread.currentThread().getName()+"\t insert blockingQ "+data+" failed ");
                }
                TimeUnit.SECONDS.sleep(1);
            }
            System.out.println(Thread.currentThread().getName()+"\t producer done ");
        }


        public void stop(){
            FLAG=false;
        }

        public void consume()throws Exception{
            String result=null;

            while(FLAG){

                result= blockingQ.poll(2,TimeUnit.SECONDS);

                if(null==result || result.equalsIgnoreCase("")){

                    FLAG=false;
                    System.out.println(Thread.currentThread().getName()+"\t consumer exceeded 2 s, consumer exit ");
                    return;
                }
                System.out.println(Thread.currentThread().getName()+"\t consumer consuming "+result+" succeeded ");
               // TimeUnit.SECONDS.sleep(1);
            }
            System.out.println(Thread.currentThread().getName()+"\t consumer done ");
        }



    }

}


@Data
@AllArgsConstructor
@NoArgsConstructor
class User{
    private String uid;
    private String name;
}

/**
 * @auther zzyy
 * @create 2020-02-20 11:55
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = OrderHystrixMain80.class)
@EnableFeignClients
@EnableHystrix
public class OrderHystrixMain80
{



     static List<Apple> appleStore=new ArrayList<>();

    static{
        appleStore.add(new Apple(1,"red",500,"KL"));
        appleStore.add(new Apple(2,"red",400,"KL"));
        appleStore.add(new Apple(3,"green",300,"KL"));
        appleStore.add(new Apple(4,"green",200,"SG"));
        appleStore.add(new Apple(5,"green",100,"SG"));

    }
    public static int reverse(int x) {
        long ans=0;  // declaring long so that overflow can be avoided
        while(x!=0){
            ans=ans*10+x%10;  // adding every digit of x to the ans from right side.  ans is reverse of x
            x/=10;
        }
        return (int)ans==ans?(int)ans:0; // if long x is equal to int x then it is in range of int else return 0
    }

    //given a string, find 1st unique char
    public  static int firstUniqChar(String s) {

        int ireturn=-1;
        LinkedHashMap<Character,Map<Integer,Integer>> map=new LinkedHashMap<>();
        for( int i=-1,acc=i,l=s.length();++i<l;){
            char ch=s.charAt(i);
            int ifinal=i;final int ifinal_=ifinal;
            System.out.println("loop ch:"+ch);


            if(map.containsKey(ch)){
                System.out.println("apply char map contains:"+ch);
                Map<Integer,Integer> mmp=map.get(ch);
                Set ks=mmp.keySet();
                int i_k=(int)ks.stream().findFirst().get();
                System.out.println("apply char map i_k:"+i_k);
                int finalsum=mmp.get(i_k)+1;

                mmp.put(i_k,finalsum);
                map.put(ch,mmp);
            }else{
                //                map.computeIfAbsent(ch,k->new HashMap<Integer,Integer>()
                //                        .put(ifinal,ifinal_));
                Map<Integer,Integer> mmsubm =new HashMap<Integer,Integer>();
                mmsubm.put(ifinal,1);
                map.put(ch,mmsubm);

            }
            //            int geti=map.computeIfAbsent(ch, new Function<Character, Integer>() {
            //                @Override
            //                public Integer apply(Character k) {
            //
            //                    int returnresi=ifinal;
            //                    System.out.println("apply char:"+k);
            //                    if(map.containsKey(k)){
            //                        System.out.println("apply char map contains:"+k);
            //                        returnresi=map.get(k)+1;
            //                    }
            //                    System.out.println("apply returnresi:"+returnresi);
            //                    return returnresi;
            //                }
            //            });

            System.out.println("ch:"+ch);


            if(i==(l-1)){
                //last loop print out the value

               try {
                   System.out.println("end map:"+map);
                   List<Character> rmap=map.entrySet().stream()
                           .filter(f->f.getValue()
                                   .entrySet().stream()
                                   .filter(k->k.getValue()==1).count()>0)
                           .map(m->m.getKey())
                           .collect(Collectors.toList());


                   ireturn=s.indexOf(rmap.iterator().next());

                   System.out.println("get rmap :"+rmap);
                   System.out.println("get acc :"+acc);
               }catch(Exception ec){
                   ec.printStackTrace();
               }


            }
        }

        System.out.println("map :"+map);

        return ireturn;
    }

    //leetcode: convert string to zigzag
    public String convertZigZag(String s, int numRows) {
        //special cases "AB" "A"
        if (numRows < 2) {
            return s;
        }
        //each row is a arraylist to store each element,so StringBuilder
        ArrayList<StringBuilder> list = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            list.add(new StringBuilder());
        }
        //row1 begin is 0
        int i = 0, reverse = -1;
        for (char c : s.toCharArray()) {
            //each element add to their own row list
            list.get(i).append(c);
            //will row number is 0 or numRows -1.switch direction
            if (i == 0 || i == numRows - 1) {
                reverse = -reverse;
            }
            i += reverse;
        }
        //connect each row of the list
        StringBuilder Builder = new StringBuilder();
        for (StringBuilder s1 : list) {
            Builder.append(s1);
        }
        return Builder.toString();
    }

    public int[] sortArrBubbleSort(int[] arr,boolean isAsc){
        int len=arr.length;
        for(int i=-1;++i<len;){
            for(int j=i;++j<len;){
                if(isAsc?arr[i]>arr[j]:arr[i]<arr[j]){
                    int tempI=arr[i];
                    arr[i]=arr[j];
                    arr[j]=tempI;
                }
            }
        }
        return arr;
    }

    public int[] sortArrPivot(int[] arr,boolean isAsc){
        int len=arr.length;
        if(len==0)return arr;

        Deque<Integer> q_=new ArrayDeque<>();
        int pivotIndex=0;
        int pivot=arr[pivotIndex];

        for(int i=0;++i<len;){
            int j=i;
            while(arr[j]<pivot && j>-1){

                int tmp=arr[j];
                arr[j]=pivot;
                pivot=tmp;
                pivotIndex=j;
                arr[j-1]=pivot;
                --j;
            }

        }
        return arr;
    }

    public double getMedian(int[] arr,boolean isAsc){
        double m_=-1;int len=arr.length;
        boolean isEvenArr=len%2==0?true:false;
        Arrays.sort(arr);
        System.out.println("isEvenArr:"+isEvenArr);System.out.println("isEvenArr len:"+len);
//         for(int i=-1;++i<len;){
//             for(int j=i;++j<len;){
//                 if(isAsc?arr[i]>arr[j]:arr[i]<arr[j]){
//                     int tempI=arr[i];
//                     arr[i]=arr[j];
//                     arr[j]=tempI;

//                 }
//             }
//         }

        for(int k=-1;++k<len;){
            if(isEvenArr){
                if(k==(len/2-1)){
                    return ((double)arr[k]+arr[1+k])/2;
                }
            }else{
                //odd array
                if(k==(len/2)){
                    return (arr[k]);
                }

            }
        }

        return m_;
    }
    //leetcode: find length of longest substring in a given string
    public int lengthOfLongestSubstring(String s) {
        if(s.length()<2){
            return s.length();
        }

        Map<Character,Integer> map = new HashMap<>();

        int start = 0;
        int maxLength = -1;

        for(int i =0; i< s.length(); i++){
            if(map.containsKey(s.charAt(i))){
                int val = map.get(s.charAt(i));
                if(val>=start){
                    start = val+1;
                    map.remove(s.charAt(i));
                }
            }

            map.put(s.charAt(i),i);
            if(maxLength < (i - start +1)){
                maxLength = i - start+1;
            }
        }

        return maxLength;

    }

    //leetcode: add 2 number
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int sum = 0;
        ListNode current = new ListNode(0);
        ListNode result = current;

        while(l1!=null || l2!=null) {

            if(l1!=null) {
                sum += l1.val;
                l1 = l1.next;
            }

            if(l2!=null) {
                sum += l2.val;
                l2 = l2.next;
            }

            current.next = new ListNode(sum % 10);
            current = current.next;

            sum = sum > 9 ? 1 : 0;
        }

        if(sum>0) {
            current.next = new ListNode(sum);
        }

        return result.next;
    }


    @Value("${os.name}")
    public String osName;

    //leetcode: calculateStr(+ - only)
    public static int calculateStr(String s) {
        int len = s.length(), sign = 1, result = 0;
        Stack<Integer> stack = new Stack<Integer>();
        for (int i = 0; i < len; i++) {
            if (Character.isDigit(s.charAt(i))) {
                int sum = s.charAt(i) - '0';//convert char to int
                //0~9 -> ASCII 48~59
                while (i + 1 < len && Character.isDigit(s.charAt(i + 1))) {
                    sum = sum * 10 + s.charAt(i + 1) - '0';
                    i++;
                }
                result += sum * sign;
            } else if (s.charAt(i) == '+')
                sign = 1;
            else if (s.charAt(i) == '-')
                sign = -1;
            else if (s.charAt(i) == '(') {
                stack.push(result);
                stack.push(sign);
                result = 0;
                sign = 1;
            } else if (s.charAt(i) == ')') {
                result = result * stack.pop() + stack.pop();
            }

        }
        return result;
    }

    static Logger logger = LoggerFactory.getLogger(OrderHystrixMain80.class);

    private static void loadInitialDrivers() {//SPI Load mechanism:
        //https://pdai.tech/md/java/advanced/java-advanced-spi.html
        /*
        1.
        在JDBC4.0之前，我们开发有连接数据库的时候，通常会用Class.forName("com.mysql.jdbc.Driver")这句先加载数据库相关的驱动，然后再进行获取连接等的操作。而JDBC4.0之后不需要用Class.forName("com.mysql.jdbc.Driver")来加载驱动，直接获取连接就可以了，现在这种方式就是使用了Java的SPI扩展机制来实现。# JDBC接口定义

        2.
        common-logging（也称Jakarta Commons Logging，缩写 JCL）是常用的日志库门面

        3.
        spring META-INF/spring.factories

        * */
        String drivers;
        try {
            drivers = AccessController.doPrivileged(new PrivilegedAction<String>() {
                public String run() {
                    return System.getProperty("jdbc.drivers");
                }
            });
        } catch (Exception ex) {
            drivers = null;
        }

        AtomicReference<Driver> driverRef = new AtomicReference<>(null);
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
                try{
                    //use SPI ServiceLoader and load the implemented driver class
                    ServiceLoader<java.sql.Driver> loadedDrivers = ServiceLoader.load(java.sql.Driver.class);
                    Iterator<java.sql.Driver> driversIterator = loadedDrivers.iterator();
                    while(driversIterator.hasNext()) {
                        driverRef.set(driversIterator.next());
                        break;
                    }
                } catch(RuntimeException t) {
                    // Do nothing
                }
                return null;
            }
        });

        if(!Objects.isNull(driverRef.get())){
            System.setProperty("jdbc.spi.driver.impl",driverRef.get().getClass().getName());
            System.out.println("DriverManager.initialize from spi loop: jdbc.drivers = " + System.getProperty("jdbc.spi.driver.impl"));
        }

        System.out.println("DriverManager.initialize: jdbc.drivers = " + drivers);

        if (drivers == null || drivers.equals("")) {
            return;
        }
        String[] driversList = drivers.split(":");
        System.out.println("number of Drivers:" + driversList.length);
        for (String aDriver : driversList) {
            try {
                System.out.println("DriverManager.Initialize: loading " + aDriver);
                Class.forName(aDriver, true,
                        ClassLoader.getSystemClassLoader());
            } catch (Exception ex) {
                System.out.println("DriverManager.Initialize: load failed: " + ex);
            }
        }
    }

    static {
        try {
            List<String> spiServices = loadFactoryNames(OrderHystrixMain80.class, null);
            for (String spiService : spiServices) {
                Class.forName(spiService);//load smart card lib
            }
            logger.debug("smart.card loaded");
        } catch (Throwable e) {
            logger.debug("smart.card load failed : {}", e);
        }
    }

    public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/services";
    // spring.factories文件的格式为：key=value1,value2,value3
    // find all the META-INF/spring.factories file from all jar
    // and interpret key=factoryClass class with all its value
    public static List<String> loadFactoryNames(Class<?> factoryClass, ClassLoader classLoader) throws IOException {
        String factoryClassName = factoryClass.getName();
        // get resource file url
        Enumeration<URL> urls = (classLoader != null ? classLoader.getResources(FACTORIES_RESOURCE_LOCATION) : ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
        List<String> result = new ArrayList<String>();
        // iterate all URL
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            // base on file url get the properties file and obtain a pair of @Configuration class
            Properties properties = PropertiesLoaderUtils.loadProperties(new UrlResource(url));
            String factoryClassNames = properties.getProperty(factoryClassName);
            // add in to list and return
            result.addAll(Arrays.asList(StringUtils.commaDelimitedListToStringArray(factoryClassNames)));
        }
        return result;
    }

    public static void main(String[] args) throws Exception
    {
        loadInitialDrivers();
        //ServiceLoader<Driver> loadedDrivers = ServiceLoader.load(Driver.class);
        libLoadCertInfoStr();
        libLoadCert();
        libLoadCertNcrypt();

        int vxor_1 = 13;
        int vxor_2 = 87;
        int vxor = vxor_1 ^ vxor_2;
        int verifyXOR2 = vxor ^ vxor_2;
        int verifyXOR1 = vxor ^ vxor_1;
        assert verifyXOR2==vxor_1;

        assert verifyXOR1==vxor_2;


        SecureString sstr = new SecureString("1234");

        System.out.println(sstr.asString());

        // SPI Service Provider Interface
        // java.util.ServiceLoader serviceLoader = new java.util.ServiceLoader();


        //SpringApplication.run(OrderHystrixMain80.class, args);
        logger.trace("OrderHystrixMain80 main entered trace",123);
        logger.debug("OrderHystrixMain80 main entered debug ",123);
        logger.info("OrderHystrixMain80 main entered info ",123);
        logger.warn("OrderHystrixMain80 main entered warn ",123);
        logger.error("OrderHystrixMain80 main entered error ",System.getProperty("LOGGING_FORMAT"));

        String testPwd_ak="12345_jason_pwd_67890";
        char[] testPwd_ak_char={'j','a','s','o','n','_','p','w','d'};


        Stack<Integer> stack = new Stack<>();

        Queue<Integer> que = new LinkedList<>();

        Deque<Integer> deque = new ArrayDeque<>();
        deque.push(1);//1 is the top
        deque.push(2);//2 is the top

        Iterator<Integer> iterator = deque.iterator();
        while (iterator.hasNext()){
            Integer a=iterator.next();
            deque.remove(a);
            //iterator.remove();
        }
        System.out.println(deque);

        que.add(1);
        que.add(2);
        stack.push(1);//1 is the top

        stack.push(2);//2 is the top



        List<Integer> list1 = stack.stream().collect(Collectors.toList());//[1,2]

        List<Integer> list2 = deque.stream().collect(Collectors.toList());//[2,1]

        ListNode ln1=new ListNode(1);
        ListNode ln2=new ListNode(2,ln1);
        ListNode ln3=new ListNode(3,ln2);

        ListNode ln4=new ListNode(4);
        ListNode ln5=new ListNode(5,ln4);

        ListNode lnr=addTwoNumbers(ln3,ln5);
        System.out.println("lnr -> "+lnr.val);

        System.out.println(  "a - 0");
        System.out.println(   'a' - '0');
        String calcStr="1+(3-2)";
        System.out.println("calculateStr: "+calculateStr(calcStr));

        int index_=firstUniqChar("bo9bkod");
        System.out.println("firstUniqChar :"+index_);

        //TreeNode.getShortestUniqueSubstring(new char[]{'x','y','z'},"xxycyzyx");
        System.out.println("TreeNode combination_ :"+TreeNode.combination_(4, 2));

        //ConcurrentHashMap<String,String> chmp=new ConcurrentHashMap<>(16,0.75,16);
        //hashset vs linkedhashset vs treeset start

        HashSet<String> fruitsStore = new HashSet<String>();
        LinkedHashSet<String> fruitMarket = new LinkedHashSet<String>();
        TreeSet<String> fruitBuzz = new TreeSet<String>();

        for(String fruit: Arrays.asList("mango", "apple", "banana")){
            fruitsStore.add(fruit);
            fruitMarket.add(fruit);
            fruitBuzz.add(fruit);
        }

        //no ordering in HashSet – elements stored in random order
        System.out.println("Ordering in HashSet :" + fruitsStore);

        //insertion order or elements – LinkedHashSet storeds elements as insertion
        System.err.println("Order of element in LinkedHashSet :" + fruitMarket);

        //should be sorted order – TreeSet stores element in sorted order
        System.out.println("Order of objects in TreeSet :" + fruitBuzz);


        //Performance test to insert 10M elements in HashSet, LinkedHashSet and TreeSet
        Set<Integer> numbers = new HashSet<Integer>();
        long startTime = System.nanoTime();
        for(int i =0; i<10000000; i++){
            numbers.add(i);
        }

        long endTime = System.nanoTime();
        System.out.println("Total time to insert 10M elements in HashSet in sec : "
                + (endTime - startTime));


        // LinkedHashSet performance Test – inserting 10M objects
        numbers = new LinkedHashSet<Integer>();
        startTime = System.nanoTime();
        for(int i =0; i<10000000; i++){
            numbers.add(i);
        }
        endTime = System.nanoTime();
        System.out.println("Total time to insert 10M elements in LinkedHashSet in sec : "
                + (endTime - startTime));

        // TreeSet performance Test – inserting 10M objects
        numbers = new TreeSet<Integer>();
        startTime = System.nanoTime();
        for(int i =0; i<10000000; i++){
            numbers.add(i);
        }
        endTime = System.nanoTime();
        System.out.println("Total time to insert 10M elements in TreeSet in sec : "
                + (endTime - startTime));

        //hashset vs linkedhashset vs treeset end
        BlockingQueue<String> bq=new LinkedBlockingQueue<>(2);
        BlockingQueue<String> aq=new ArrayBlockingQueue<>(2);

        aq.offer("a");
        aq.offer("b");
        System.out.println("===blockin q====");
        aq.offer("c",2,TimeUnit.SECONDS);
        System.out.println("===blockin q=c====");
        //q.stream sort

        Map<String,List<Apple>> dfs=new HashMap<>();

        for (Apple apple : appleStore) {
            List<Apple> dlist=dfs.computeIfAbsent(apple.getColor(),k->new ArrayList<>());
            dlist.add(apple);
        }
        System.out.println(dfs);
        System.out.println("================old================");
        System.out.println("================old================");
        dfs=appleStore
                .stream()
                .collect(Collectors.groupingBy(g->g.getColor()));
        System.out.println(dfs);

        System.out.println("================averaging map================");
        Map<String,Double> mapAvg=appleStore
                .stream()
                .filter(new Predicate<Apple>() {
                    @Override
                    public boolean test(Apple apple) {
                        return apple.getWeight()>100;
                    }
                })
                .map(new Function<Apple, Apple>() {
                    @Override
                    public Apple apply(Apple apple) {
                        apple.setWeight(apple.getWeight()+1);
                        return apple;
                    }
                })
                .collect(Collectors.groupingBy(g->g.getColor(),
                        Collectors.averagingDouble(a->a.getWeight())))
                ;

//        .forEach((k,v)->{
//            System.out.println("k : "+k+" | v : "+v+" ");
//        })
        System.out.println(mapAvg);




        List<Integer> lint=Arrays.asList(12,20,35,46,55,68,75);

        int ires=lint.stream().filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer i) {
                System.out.println("filtering integer:"+i);
                return i%5==0;
            }
        }).map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                System.out.println("mapping integer:"+integer);
                return integer*=2;
            }
        }).findFirst().orElse(0);
        System.out.println("stream get ires:"+ires);

        System.out.println("================aaa================");
        System.out.println("================aaa================");

        System.out.println(OrderHystrixMain80.class.getClassLoader());

        //a. sum map
        int sumLimit=20;
        int[] intarr= new int[]{1,19,3,17,5,2,4};
        Map<Integer, Integer> mapSum=new HashMap<>();
        for(int i=intarr.length;--i>-1;){
            if(mapSum.containsKey(intarr[i])){
                System.out.println(intarr[i]+" & "+mapSum.get(intarr[i]));
            }else{
                mapSum.put(sumLimit-intarr[i],intarr[i]);
            }
        }



        //1.SoftReference eg start
        User u1=new User();
        WeakReference<User> softU=new WeakReference<User>(u1);
        u1=null;
        System.out.println(softU.get());
        Runtime.getRuntime().gc();
        //note that softReference will survive after gc if there is sufficient memory,
        //but weakReference will somehow sweep away the item once gc is carried out
        System.out.println("After gc");
        System.out.println(softU.get());
        List<byte[]> lb= new LinkedList<>();
        try{
            for(int j=0;++j<100;){
                System.out.println(softU.get());
                lb.add(new byte[1024*1024*1]);
            }
        }catch (Throwable ec){
            System.out.println(softU.get());
            ec.printStackTrace();
            System.out.println(ec.getMessage());
        }
        //1.SoftReference eg end
        System.out.println("=================SoftReference end===================");
        System.out.println("=================SoftReference end===================");
        System.out.println("=================SoftReference end===================");






        java.util.List<Integer> values = new java.util.ArrayList<>();
        for(int i=0;i++<100;){
            //System.out.print(i);
            values.add(i);
        }

        values.stream().filter(f->{

            System.out.print(f);
            return true;
        }).findAny();


        System.out.println(" OrderHystrixMain80 main =>");
        //demoa ad;//= new demoa();//  demoa ad1= new demoa();
        Map<String,String> map_1= new HashMap<String, String>() {{  put("1", "ValA");  put("2", "ValA");  }};
        Map<String,String> map_2= new HashMap<String, String>() {{  put("1", "ValA");  put("3", "ValD");  }};
        Map<String,String> map_All= new HashMap<String, String>();

        map_All.putAll(map_1);map_All.putAll(map_2);

        String[] names1 = new String[] {"Ava", "Emma", "Olivia"};
        String[] names2 = new String[] {"Olivia", "Sophia", "Emma"};
        HashSet<String> hsset=new HashSet<String>(Arrays.asList(names1));

        String[] arr_=hsset.toArray(new String[]{});

        System.out.println(Collections.synchronizedList(Arrays.asList(arr_)));

        map_All.entrySet().stream().forEach(f->f.setValue("ok"));
        Optional<Map.Entry<String,String>>
                first_=
                map_All.entrySet().stream().filter(f->f.getKey()=="1").findFirst();
        System.out.println(first_);
        System.out.println(first_.hashCode());



        Set<String> newVariable = new HashSet<>(map_All.keySet());
        System.out.println("newVariable set");
        System.out.println(newVariable);

        for (Map.Entry<String, String> entry : map_All.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key+" key");
            System.out.println(value+" vl");
        }


        SpringApplication app = new SpringApplication(OrderHystrixMain80.class);
        app.setWebApplicationType(WebApplicationType.SERVLET); //<<<<<<<<<
        app.setBeanNameGenerator(new BeanNameGen());
        ConfigurableApplicationContext run = app.run(args);

//        ApplicationContext ctx = new SpringApplicationBuilder(OrderHystrixMain80.class)
//                .beanNameGenerator(new BeanNameGen())
//                .run(args);

        //ConfigurableApplicationContext run = SpringApplication.run(OrderHystrixMain80.class, args);

        System.out.println("beanDefinitionCount: "+run.getBeanDefinitionCount());
        System.out.println("=======================getBeanDefinitionNames=========================");
        System.out.println("=======================getBeanDefinitionNames=========================");
        for (String beanDefinitionName : run.getBeanDefinitionNames()) {
            System.out.println("beanDefinitionName: "+beanDefinitionName);
        }

        for (String beanController : run.getBeanNamesForAnnotation(RestController.class)) {
            System.out.println("controller bean: "+beanController);//com.atguigu.springcloud.controller.asyncController
            //com.atguigu.springcloud.controller.employeeDataController
            //com.atguigu.springcloud.controller.orderHystirxController
        }


        //2.Callable fulturetask start

        class MyCallable implements Callable<String> {

            private long waitTime;

            public MyCallable(int timeInMillis){
                this.waitTime=timeInMillis;
            }
            @Override
            public String call() throws Exception {
                Thread.sleep(waitTime);
                //return the thread name executing this callable task
                return Thread.currentThread().getName();
            }

        }


        MyCallable callable1 = new MyCallable(1000);
        MyCallable callable2 = new MyCallable(2000);

        FutureTask<String> futureTask1 = new FutureTask<String>(callable1);
        FutureTask<String> futureTask2 = new FutureTask<String>(callable2);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(futureTask1);
        executor.execute(futureTask2);

        while (true) {
            try {
                if(futureTask1.isDone() && futureTask2.isDone()){
                    System.out.println("Done");
                    //shut down executor service
                    executor.shutdown();
                    return;
                }

                if(!futureTask1.isDone()){
                    //wait indefinitely for future task to complete
                    System.out.println("FutureTask1 output="+futureTask1.get());
                }

                System.out.println("Waiting for FutureTask2 to complete");
                String s = futureTask2.get(200L, TimeUnit.MILLISECONDS);
                if(s !=null){
                    System.out.println("FutureTask2 output="+s);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }catch(TimeoutException e){
                //do nothing
            }
        }
        //2.Callable futureTask end

    }


}

